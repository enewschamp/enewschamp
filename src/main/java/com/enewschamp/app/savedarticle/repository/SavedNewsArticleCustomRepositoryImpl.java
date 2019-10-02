package com.enewschamp.app.savedarticle.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSearchRequest;
import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSummaryDTO;
import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.domain.service.RepositoryImpl;

@Repository
public class SavedNewsArticleCustomRepositoryImpl extends RepositoryImpl implements SavedNewsArticleCustomRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<SavedNewsArticleSummaryDTO> findArticles(SavedNewsArticleSearchRequest searchRequest,
			Pageable pageable) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SavedNewsArticleSummaryDTO> criteriaQuery = cb.createQuery(SavedNewsArticleSummaryDTO.class);
		Root<NewsArticleGroup> articleGroupRoot = criteriaQuery.from(NewsArticleGroup.class);
		Root<NewsArticle> articleRoot = criteriaQuery.from(NewsArticle.class);
		Root<StudentActivity> studentActivityRoot = criteriaQuery.from(StudentActivity.class);

		criteriaQuery = criteriaQuery.select(cb.construct(SavedNewsArticleSummaryDTO.class, articleRoot.get("newsArticleId"),
				 articleRoot.get("publishDate"), 
				articleGroupRoot.get("genreId"), articleGroupRoot.get("headline"), studentActivityRoot.get("opinion")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

		filterPredicates
				.add(cb.equal(articleGroupRoot.get("newsArticleGroupId"), articleRoot.get("newsArticleGroupId")));
		filterPredicates.add(cb.equal(studentActivityRoot.get("newsArticleId"), articleRoot.get("newsArticleId")));
		filterPredicates.add(cb.notEqual(studentActivityRoot.get("opinion"), ""));

		if (searchRequest.getPublicationDate() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("publishDate"), searchRequest.getPublicationDate()));
		}

		if (searchRequest.getGenreId() != null) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("genreId"), searchRequest.getGenreId()));
		}
		if (searchRequest.getEditionId() != null) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("editionId"), searchRequest.getEditionId()));
		}
		if (searchRequest.getHeadline() != null) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("headline"), searchRequest.getHeadline()));
		}

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));

		// Build query
		TypedQuery<SavedNewsArticleSummaryDTO> q = entityManager.createQuery(criteriaQuery);

		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());

		}
		List<SavedNewsArticleSummaryDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, articleRoot);

		return new PageImpl<>(list, pageable, count);
	}
	@Override
	public Page<SavedNewsArticleSummaryDTO> findArticlesWithOpinions(SavedNewsArticleSearchRequest searchRequest,
			Pageable pageable) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SavedNewsArticleSummaryDTO> criteriaQuery = cb.createQuery(SavedNewsArticleSummaryDTO.class);
		Root<NewsArticleGroup> articleGroupRoot = criteriaQuery.from(NewsArticleGroup.class);
		Root<NewsArticle> articleRoot = criteriaQuery.from(NewsArticle.class);
		Root<StudentActivity> studentActivityRoot = criteriaQuery.from(StudentActivity.class);

		criteriaQuery = criteriaQuery.select(cb.construct(SavedNewsArticleSummaryDTO.class, articleRoot.get("newsArticleId"),
				 articleRoot.get("publishDate"), 
				articleGroupRoot.get("genreId"), articleGroupRoot.get("headline"), studentActivityRoot.get("opinion")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

		filterPredicates
				.add(cb.equal(articleGroupRoot.get("newsArticleGroupId"), articleRoot.get("newsArticleGroupId")));
		filterPredicates.add(cb.equal(studentActivityRoot.get("newsArticleId"), articleRoot.get("newsArticleId")));
		filterPredicates.add(cb.notEqual(studentActivityRoot.get("opinion"), ""));

		if (searchRequest.getPublicationDate() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("publishDate"), searchRequest.getPublicationDate()));
		}

		if (searchRequest.getGenreId() != null) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("genreId"), searchRequest.getGenreId()));
		}
		if (searchRequest.getEditionId() != null) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("editionId"), searchRequest.getEditionId()));
		}
		if (searchRequest.getHeadline() != null) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("headline"), searchRequest.getHeadline()));
		}

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(articleRoot.get("operationDateTime")));
		
		// Build query
		TypedQuery<SavedNewsArticleSummaryDTO> q = entityManager.createQuery(criteriaQuery);

		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());

		}
		List<SavedNewsArticleSummaryDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, articleRoot);

		return new PageImpl<>(list, pageable, count);
	}
}
