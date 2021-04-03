package com.enewschamp.app.admin.entitlement.repository;

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
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.repository.IGenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class EntitlementRepositoryCustomImpl extends RepositoryImpl implements IGenericListRepository<Entitlement>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Entitlement> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Entitlement> criteriaQuery = cb.createQuery(Entitlement.class);
		Root<Entitlement> entitlementRoot = criteriaQuery.from(Entitlement.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		if (!StringUtils.isEmpty(searchRequest.getUserId()))
			filterPredicates.add(cb.equal(entitlementRoot.get(USER_ID), searchRequest.getUserId()));

		if (!StringUtils.isEmpty(searchRequest.getPageName()))
			filterPredicates.add(cb.equal(entitlementRoot.get(PAGE_NAME), searchRequest.getPageName()));

		if (!StringUtils.isEmpty(searchRequest.getRole()))
			filterPredicates.add(cb.equal(entitlementRoot.get(ROLE), searchRequest.getRole()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(entitlementRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<Entitlement> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<Entitlement> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, entitlementRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
