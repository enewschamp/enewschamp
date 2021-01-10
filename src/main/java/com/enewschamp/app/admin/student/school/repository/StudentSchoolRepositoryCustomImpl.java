package com.enewschamp.app.admin.student.school.repository;

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
import com.enewschamp.domain.repository.RepositoryImpl;
import com.enewschamp.subscription.domain.entity.StudentSchool;

@Repository
public class StudentSchoolRepositoryCustomImpl extends RepositoryImpl implements StudentSchoolRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<StudentSchool> findStudentSchools(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<StudentSchool> criteriaQuery = cb.createQuery(StudentSchool.class);
		Root<StudentSchool> studentSchoolsRoot = criteriaQuery.from(StudentSchool.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentSchoolsRoot.get("studentId"), searchRequest.getStudentId()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(studentSchoolsRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<StudentSchool> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<StudentSchool> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, studentSchoolsRoot);
		return new PageImpl<>(list, pageable, count);
	}

}