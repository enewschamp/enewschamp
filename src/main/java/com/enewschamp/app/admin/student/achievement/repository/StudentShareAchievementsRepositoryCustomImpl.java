package com.enewschamp.app.admin.student.achievement.repository;

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
import com.enewschamp.app.common.repository.GenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;
import com.enewschamp.subscription.domain.entity.StudentShareAchievements;

@Repository
public class StudentShareAchievementsRepositoryCustomImpl extends RepositoryImpl
		implements GenericListRepository<StudentShareAchievements> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<StudentShareAchievements> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<StudentShareAchievements> criteriaQuery = cb.createQuery(StudentShareAchievements.class);
		Root<StudentShareAchievements> studentachievementRoot = criteriaQuery.from(StudentShareAchievements.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentachievementRoot.get("studentId"), searchRequest.getStudentId()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(studentachievementRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<StudentShareAchievements> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<StudentShareAchievements> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, studentachievementRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
