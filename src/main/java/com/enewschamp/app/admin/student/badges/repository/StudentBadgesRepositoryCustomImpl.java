package com.enewschamp.app.admin.student.badges.repository;

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
import com.enewschamp.app.student.badges.entity.StudentBadges;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class StudentBadgesRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<StudentBadges>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<StudentBadges> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<StudentBadges> criteriaQuery = cb.createQuery(StudentBadges.class);
		Root<StudentBadges> studentachievementRoot = criteriaQuery.from(StudentBadges.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentachievementRoot.get(STUDENT_ID), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getStudentBadgesId()))
			filterPredicates
					.add(cb.equal(studentachievementRoot.get(STUDENT_BADGES_ID), searchRequest.getStudentBadgesId()));

		if (!StringUtils.isEmpty(searchRequest.getYearMonth()))
			filterPredicates
					.add(cb.like(studentachievementRoot.get(YEAR_MONTH), '%' + searchRequest.getYearMonth() + '%'));

		if (!StringUtils.isEmpty(searchRequest.getBadgeId()))
			filterPredicates.add(cb.equal(studentachievementRoot.get(BADGE_ID), searchRequest.getBadgeId()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(studentachievementRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<StudentBadges> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<StudentBadges> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, studentachievementRoot);
		return new PageImpl<>(list, pageable, count);
	}

}