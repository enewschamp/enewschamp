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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;

public class NewsArticleRepositoryImpl implements NewsArticleRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<NewsArticleDTO> findAllPage(NewsArticleSearchRequest searchRequest, Pageable pageable) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<NewsArticleDTO> query = cb.createQuery(NewsArticleDTO.class);
		Root<NewsArticle> articleRoot = query.from(NewsArticle.class);
		//Root<Author> authorRoot = query.from(Author.class);

		List<Predicate> filterPredicates = new ArrayList<>();
		query.select(cb.construct(NewsArticleDTO.class, 
								  articleRoot.get("newsArticleId"), 
								  articleRoot.get("newsArticleGroupId"),
								  articleRoot.get("readingLevel")));
				//.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		
		TypedQuery<NewsArticleDTO> q = entityManager.createQuery(query);
		
		if(pageable.getPageSize() > 0) {
			q.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		return new PageImpl<>(q.getResultList(), pageable, getAllCount(searchRequest));
	}

	private Long getAllCount(NewsArticleSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<NewsArticle> articleRoot = query.from(NewsArticle.class);

		List<Predicate> filterPredicates = new ArrayList<>();
		query.select(cb.count(articleRoot));
		//.where(cb.and((Predicate[])filterPredicates.toArray(new Predicate[0])));
		return (Long) entityManager.createQuery(query).getSingleResult();
	}
}
