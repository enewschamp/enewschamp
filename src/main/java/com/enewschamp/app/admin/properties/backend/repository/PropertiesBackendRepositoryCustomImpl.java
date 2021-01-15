package com.enewschamp.app.admin.properties.backend.repository;

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
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.common.domain.entity.PropertiesBackend;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class PropertiesBackendRepositoryCustomImpl extends RepositoryImpl implements PropertiesBackendRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<PropertiesBackend> findPropertiesBackends(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PropertiesBackend> criteriaQuery = cb.createQuery(PropertiesBackend.class);
		Root<PropertiesBackend> propertiesBackendRoot = criteriaQuery.from(PropertiesBackend.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getPropertyId()))
			filterPredicates.add(cb.equal(propertiesBackendRoot.get("propertyId"), searchRequest.getPropertyId()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(propertiesBackendRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<PropertiesBackend> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<PropertiesBackend> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, propertiesBackendRoot);
		return new PageImpl<>(list, pageable, count);
	}

}

