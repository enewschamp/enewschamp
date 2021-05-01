package com.enewschamp.app.admin.publication.monthly.repository;

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
import org.springframework.util.StringUtils;

import com.enewschamp.app.admin.AdminConstant;
import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.article.monthly.ArticlePublicationMonthlyGenre;
import com.enewschamp.app.common.repository.IGenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class PublicationMonthySummaryRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<ArticlePublicationMonthlyGenre>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<ArticlePublicationMonthlyGenre> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ArticlePublicationMonthlyGenre> criteriaQuery = cb
				.createQuery(ArticlePublicationMonthlyGenre.class);
		Root<ArticlePublicationMonthlyGenre> monthlySummaryRoot = criteriaQuery
				.from(ArticlePublicationMonthlyGenre.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getReadingLevel()))
			filterPredicates.add(cb.equal(monthlySummaryRoot.get(READING_LEVEL), searchRequest.getReadingLevel()));

		if (!StringUtils.isEmpty(searchRequest.getYearMonth()))
			filterPredicates.add(cb.equal(monthlySummaryRoot.get(YEAR_MONTH), searchRequest.getYearMonth()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(monthlySummaryRoot.get(YEAR_MONTH)));
		// Build query
		TypedQuery<ArticlePublicationMonthlyGenre> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<ArticlePublicationMonthlyGenre> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, monthlySummaryRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
