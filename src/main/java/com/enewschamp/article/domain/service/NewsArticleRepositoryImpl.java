package com.enewschamp.article.domain.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.query.criteria.internal.path.RootImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.domain.common.AppConstants;

public class NewsArticleRepositoryImpl implements NewsArticleRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<NewsArticleSummaryDTO> findArticles(NewsArticleSearchRequest searchRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<NewsArticleSummaryDTO> criteriaQuery = cb.createQuery(NewsArticleSummaryDTO.class);
		Root<NewsArticleGroup> articleGroupRoot = criteriaQuery.from(NewsArticleGroup.class);
		Root<NewsArticle> articleRoot = criteriaQuery.from(NewsArticle.class);

		criteriaQuery = criteriaQuery.select(cb.construct(NewsArticleSummaryDTO.class, 
				  articleRoot.get("newsArticleId"), 
				  articleRoot.get("newsArticleGroupId"),
				  articleRoot.get("publishDate"),
				  articleRoot.get("readingLevel"),
				  articleRoot.get("authorId"),
				  articleRoot.get("editorId"),
				  articleRoot.get("publisherId"),
				  articleRoot.get("status"),
				  articleGroupRoot.get("genreId"),
				  articleGroupRoot.get("headline"),
				  articleGroupRoot.get("imagePathMobile"),
				  articleGroupRoot.get("imagePathTab"), 
				  articleGroupRoot.get("imagePathDesktop")));
		
		List<Predicate> filterPredicates = new ArrayList<>();

		filterPredicates.add(cb.equal(articleGroupRoot.get("newsArticleGroupId"), articleRoot.get("newsArticleGroupId")));
		
		if (searchRequest.getArticleId() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("newsArticleId"), searchRequest.getArticleId()));
		}
		if (searchRequest.getArticleGroupId() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("newsArticleGroupId"), searchRequest.getArticleGroupId()));
		}
		if (searchRequest.getPublicationDate() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("publishDate"), searchRequest.getPublicationDate()));
		}
		
		if (searchRequest.getPublicationDateFrom() != null) {
			filterPredicates.add(cb.greaterThanOrEqualTo(articleRoot.get("publishDate"), searchRequest.getPublicationDateFrom()));
		}
		if (searchRequest.getPublicationDateTo() != null) {
			filterPredicates.add(cb.lessThan(articleRoot.get("publishDate"), searchRequest.getPublicationDateTo()));
		}
		Predicate readingLevelPredicate = buildReadingLevelPredicate(searchRequest, cb, articleRoot);
		if(readingLevelPredicate != null) {
			filterPredicates.add(readingLevelPredicate);
		}
		if (searchRequest.getAuthorId() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("authorId"), searchRequest.getAuthorId()));
		}
		if (searchRequest.getEditorId() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("editorId"), searchRequest.getEditorId()));
		}
		if (searchRequest.getPublisherId() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("publisherId"), searchRequest.getPublisherId()));
		}
		if (searchRequest.getArticleStatusList() != null && searchRequest.getArticleStatusList().size() > 0) {
			filterPredicates.add(articleRoot.get("status").in(searchRequest.getArticleStatusList()));
		}
		if (searchRequest.getGenreId() != null) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("genreId"), searchRequest.getGenreId()));
		}
		if (searchRequest.getEditionId() != null) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("editionId"), searchRequest.getEditionId()));
		}
		if (searchRequest.getHeadline() != null) {
			filterPredicates.add(cb.like(articleGroupRoot.get("headline"), "%" + searchRequest.getHeadline() + "%"));
		}
		if (searchRequest.getCredits() != null) {
			filterPredicates.add(cb.like(articleGroupRoot.get("credits"), "%" + searchRequest.getCredits() + "%"));
		}
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		
		TypedQuery<NewsArticleSummaryDTO> q = entityManager.createQuery(criteriaQuery);
		
		if(pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
			
		}
		List<NewsArticleSummaryDTO> list = q.getResultList();
		long count = getAllCount(searchRequest, criteriaQuery, filterPredicates, articleRoot);
		
		return new PageImpl<>(list, pageable, count);
	}

	private Predicate buildReadingLevelPredicate(NewsArticleSearchRequest searchRequest, 
												 CriteriaBuilder cb,
												 Root<NewsArticle> articleRoot) {
		List<Predicate> readingLevelPredicates = new ArrayList<>();
		if (searchRequest.getReadingLevel1() != null) {
			if(searchRequest.getReadingLevel1().equals(AppConstants.YES)) {
				readingLevelPredicates.add(cb.equal(articleRoot.get("readingLevel"), 1));
			} else {
				readingLevelPredicates.add(cb.notEqual(articleRoot.get("readingLevel"), 1));
			}
		}
		if (searchRequest.getReadingLevel2() != null) {
			if(searchRequest.getReadingLevel2().equals(AppConstants.YES)) {
				readingLevelPredicates.add(cb.equal(articleRoot.get("readingLevel"), 2));
			} else {
				readingLevelPredicates.add(cb.notEqual(articleRoot.get("readingLevel"), 2));
			}
		}
		if (searchRequest.getReadingLevel3() != null) {
			if(searchRequest.getReadingLevel3().equals(AppConstants.YES)) {
				readingLevelPredicates.add(cb.equal(articleRoot.get("readingLevel"), 3));
			} else {
				readingLevelPredicates.add(cb.notEqual(articleRoot.get("readingLevel"), 3));
			}
		}
		if (searchRequest.getReadingLevel4() != null) {
			if(searchRequest.getReadingLevel4().equals(AppConstants.YES)) {
				readingLevelPredicates.add(cb.equal(articleRoot.get("readingLevel"), 4));
			} else {
				readingLevelPredicates.add(cb.notEqual(articleRoot.get("readingLevel"), 4));
			}
		}
		Predicate predicate = null;
		if(readingLevelPredicates.size() > 0) {
			predicate = cb.or(readingLevelPredicates.toArray(new Predicate[0]));
		}
		return predicate;
	}
	
	private Long getAllCount(NewsArticleSearchRequest searchRequest, 
							 CriteriaQuery criteriaQuery, 
							 List<Predicate> filterPredicates, 
							 Root originalRoot) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		
		Root mainRoot = countQuery.from(originalRoot.getJavaType());
		
		if(criteriaQuery.getRoots() != null) {
			for(Object root: criteriaQuery.getRoots()) {
				RootImpl rootImpl = (RootImpl) root;
				if(!rootImpl.getJavaType().equals(mainRoot.getJavaType())) {
					countQuery.from(rootImpl.getJavaType());
				}
			}
		}
		
		countQuery.select(cb.count(mainRoot))
					.where(cb.and((Predicate[])filterPredicates.toArray(new Predicate[0])));
		return (Long) entityManager.createQuery(countQuery).getSingleResult();
	}

}
