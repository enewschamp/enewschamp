package com.enewschamp.app.admin.publication.daily.repository;

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

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.common.repository.IGenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;
import com.enewschamp.publication.domain.entity.PublicationDailySummary;

@Repository
public class PublicationDailySummaryRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<PublicationDailySummary> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<PublicationDailySummary> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PublicationDailySummary> criteriaQuery = cb.createQuery(PublicationDailySummary.class);
		Root<PublicationDailySummary> dailySummaryRoot = criteriaQuery.from(PublicationDailySummary.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getReadingLevel()))
			filterPredicates.add(cb.equal(dailySummaryRoot.get("readingLevel"), searchRequest.getReadingLevel()));

		if (!StringUtils.isEmpty(searchRequest.getPublicationDate()))
			filterPredicates.add(cb.equal(dailySummaryRoot.get("publicationDate"), searchRequest.getPublicationDate()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(dailySummaryRoot.get("publicationDate")));
		// Build query
		TypedQuery<PublicationDailySummary> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<PublicationDailySummary> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, dailySummaryRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
