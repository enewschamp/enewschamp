package com.enewschamp.app.admin.publication.daily.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.article.daily.ArticlePublicationDaily;
import com.enewschamp.app.common.repository.IGenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class PublicationDailySummaryRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<ArticlePublicationDaily> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<ArticlePublicationDaily> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ArticlePublicationDaily> criteriaQuery = cb.createQuery(ArticlePublicationDaily.class);
		Root<ArticlePublicationDaily> dailySummaryRoot = criteriaQuery.from(ArticlePublicationDaily.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getReadingLevel()))
			filterPredicates.add(cb.equal(dailySummaryRoot.get("readingLevel"), searchRequest.getReadingLevel()));

		if (!StringUtils.isEmpty(searchRequest.getPublicationDate()))
			filterPredicates.add(cb.equal(dailySummaryRoot.get("publicationDate"), searchRequest.getPublicationDate()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(dailySummaryRoot.get("publicationDate")));
		// Build query
		TypedQuery<ArticlePublicationDaily> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<ArticlePublicationDaily> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, dailySummaryRoot);
		return new PageImpl<>(list, pageable, count);
	}
	
	public Page<ArticlePublicationDaily> findByNative(Pageable pageable, AdminSearchRequest searchRequest) {
		String readingLevel = searchRequest.getReadingLevel();
		String yearMonth = searchRequest.getYearMonth();
		Query query = entityManager.createNativeQuery(
				//"SELECT publication_date,reading_level,articles_published,quiz_published FROM daily_published_articles_vw WHERE reading_level=? and publication_date=? ORDER BY publication_date desc",
				"SELECT publication_date,reading_level,articles_published,quiz_published FROM daily_published_articles_vw  ORDER BY publication_date desc",

				ArticlePublicationDaily.class);
		//query.setParameter(1, yearMonth);
		//query.setParameter(2, readingLevel);
		long count = query.getResultList().size();
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			query.setFirstResult(pageNumber * pageable.getPageSize());
			query.setMaxResults(pageable.getPageSize());

		}
		List<ArticlePublicationDaily> list = query.getResultList();
		return new PageImpl<>(list, pageable, count);
	}

}
