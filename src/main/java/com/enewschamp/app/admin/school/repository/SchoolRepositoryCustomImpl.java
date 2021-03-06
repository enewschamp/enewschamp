package com.enewschamp.app.admin.school.repository;

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
import com.enewschamp.app.school.entity.School;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class SchoolRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<School>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<School> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<School> criteriaQuery = cb.createQuery(School.class);
		Root<School> schoolRoot = criteriaQuery.from(School.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		if (!StringUtils.isEmpty(searchRequest.getSchoolChainId()))
			filterPredicates.add(cb.equal(schoolRoot.get(SCHOOL_CHAIN_ID), searchRequest.getSchoolChainId()));

		if (!StringUtils.isEmpty(searchRequest.getName()))
			filterPredicates.add(cb.like(schoolRoot.get(NAME), "%" + searchRequest.getName() + "%"));

		if (!StringUtils.isEmpty(searchRequest.getCountryId()))
			filterPredicates.add(cb.equal(schoolRoot.get(COUNTRY_ID), searchRequest.getCountryId()));

		if (!StringUtils.isEmpty(searchRequest.getStateId()))
			filterPredicates.add(cb.equal(schoolRoot.get(STATE_ID), searchRequest.getStateId()));

		if (!StringUtils.isEmpty(searchRequest.getCityId()))
			filterPredicates.add(cb.equal(schoolRoot.get(CITY_ID), searchRequest.getCityId()));

		if (!StringUtils.isEmpty(searchRequest.getEduBoard()))
			filterPredicates.add(cb.equal(schoolRoot.get(EDUCATION_BOARD), searchRequest.getEduBoard()));

		if (!StringUtils.isEmpty(searchRequest.getOwnership()))
			filterPredicates.add(cb.equal(schoolRoot.get(OWNERSHIP), searchRequest.getOwnership()));

		if (!StringUtils.isEmpty(searchRequest.getSchoolProgram()))
			filterPredicates.add(cb.equal(schoolRoot.get(SCHOOL_PROGRAM), searchRequest.getSchoolProgram()));

		if (!StringUtils.isEmpty(searchRequest.getSchoolProgramCode()))
			filterPredicates.add(cb.equal(schoolRoot.get(SCHOOL_PROGRAM_CODE), searchRequest.getSchoolProgramCode()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(schoolRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<School> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<School> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, schoolRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
