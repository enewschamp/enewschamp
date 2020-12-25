package com.enewschamp.app.admin.pricing.repository;

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

import com.enewschamp.domain.repository.RepositoryImpl;
import com.enewschamp.subscription.pricing.entity.IndividualPricing;
@Repository
public class IndividualPricingRepositoryCustomImpl extends RepositoryImpl implements IndividualPricingRepositoryCustom{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<IndividualPricing> findIndividualPricings(Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<IndividualPricing> criteriaQuery = cb.createQuery(IndividualPricing.class);
		Root<IndividualPricing> stateRoot = criteriaQuery.from(IndividualPricing.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		// Build query
		TypedQuery<IndividualPricing> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<IndividualPricing> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, stateRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
