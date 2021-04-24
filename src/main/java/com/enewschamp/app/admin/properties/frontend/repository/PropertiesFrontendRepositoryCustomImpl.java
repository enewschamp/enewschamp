package com.enewschamp.app.admin.properties.frontend.repository;

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
import com.enewschamp.common.domain.entity.PropertiesFrontend;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class PropertiesFrontendRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<PropertiesFrontend>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<PropertiesFrontend> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PropertiesFrontend> criteriaQuery = cb.createQuery(PropertiesFrontend.class);
		Root<PropertiesFrontend> propertiesFrontendRoot = criteriaQuery.from(PropertiesFrontend.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getPropertyId()))
			filterPredicates.add(cb.equal(propertiesFrontendRoot.get(PROPERTY_ID), searchRequest.getPropertyId()));

		if (!StringUtils.isEmpty(searchRequest.getAppName()))
			filterPredicates.add(cb.like(propertiesFrontendRoot.get(APP_NAME), "%" + searchRequest.getAppName() + "%"));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(propertiesFrontendRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<PropertiesFrontend> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<PropertiesFrontend> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, propertiesFrontendRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
