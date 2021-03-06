package com.enewschamp.app.admin.institution.repository;

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
import com.enewschamp.app.admin.institution.entity.InstitutionAddress;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.repository.IGenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class InstitutionAddressRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<InstitutionAddress>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<InstitutionAddress> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<InstitutionAddress> criteriaQuery = cb.createQuery(InstitutionAddress.class);
		Root<InstitutionAddress> instAddressRoot = criteriaQuery.from(InstitutionAddress.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getInstitutionId()))
			filterPredicates.add(cb.equal(instAddressRoot.get(INSTITUTION_ID), searchRequest.getInstitutionId()));

		if (!StringUtils.isEmpty(searchRequest.getInstitutionType()))
			filterPredicates.add(cb.equal(instAddressRoot.get(INSTITUTION_TYPE), searchRequest.getInstitutionType()));

		if (!StringUtils.isEmpty(searchRequest.getCountryId()))
			filterPredicates.add(cb.equal(instAddressRoot.get(COUNTRY_ID), searchRequest.getCountryId()));

		if (!StringUtils.isEmpty(searchRequest.getStateId()))
			filterPredicates.add(cb.equal(instAddressRoot.get(STATE_ID), searchRequest.getStateId()));

		if (!StringUtils.isEmpty(searchRequest.getCityId()))
			filterPredicates.add(cb.equal(instAddressRoot.get(CITY_ID), searchRequest.getCityId()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(instAddressRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<InstitutionAddress> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<InstitutionAddress> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, instAddressRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
