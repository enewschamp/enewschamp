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

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.school.entity.School;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class SchoolRepositoryCustomImpl extends RepositoryImpl implements SchoolRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<School> findSchools(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<School> criteriaQuery = cb.createQuery(School.class);
		Root<School> schoolRoot = criteriaQuery.from(School.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(schoolRoot.get("schoolChainId"), searchRequest.getSchoolChainId()));

		if (!StringUtils.isEmpty(searchRequest.getCategoryId()))
			filterPredicates.add(cb.like(schoolRoot.get("name"), "%" + searchRequest.getName() + "%"));

		if (!StringUtils.isEmpty(searchRequest.getCloseFlag()))
			filterPredicates.add(cb.equal(schoolRoot.get("countryId"), searchRequest.getCountryId()));

		if (!StringUtils.isEmpty(searchRequest.getSupportUserId()))
			filterPredicates.add(cb.equal(schoolRoot.get("stateId"), searchRequest.getStateId()));

		if (!StringUtils.isEmpty(searchRequest.getSupportUserId()))
			filterPredicates.add(cb.equal(schoolRoot.get("cityId"), searchRequest.getCityId()));

		if (!StringUtils.isEmpty(searchRequest.getSupportUserId()))
			filterPredicates.add(cb.equal(schoolRoot.get("eduBoard"), searchRequest.getEduBoard()));

		if (!StringUtils.isEmpty(searchRequest.getSupportUserId()))
			filterPredicates.add(cb.equal(schoolRoot.get("ownership"), searchRequest.getOwnership()));

		if (!StringUtils.isEmpty(searchRequest.getSupportUserId()))
			filterPredicates.add(cb.equal(schoolRoot.get("schoolProgram"), searchRequest.getSchoolProgram()));

		if (!StringUtils.isEmpty(searchRequest.getSupportUserId()))
			filterPredicates.add(cb.equal(schoolRoot.get("schoolProgramCode"), searchRequest.getSchoolProgramCode()));

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
