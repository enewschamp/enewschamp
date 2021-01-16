package com.enewschamp.app.admin.student.scores.monthly.total.repository;

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

@Repository
public class StudentScoresMonthlyTotalRepositoryCustomImpl extends RepositoryImpl implements StudentScoresMonthlyTotalRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<StudentScoresMonthlyTotal> findStudentMonthlyScoresTotals(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<StudentScoresMonthlyTotal> criteriaQuery = cb.createQuery(StudentScoresMonthlyTotal.class);
		Root<StudentScoresMonthlyTotal> studentScoresDailyRoot = criteriaQuery.from(StudentScoresMonthlyTotal.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentScoresDailyRoot.get("studentId"), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getEditionId()))
			filterPredicates.add(cb.equal(studentScoresDailyRoot.get("editionId"), searchRequest.getEditionId()));

		if (!StringUtils.isEmpty(searchRequest.getReadingLevel()))
			filterPredicates.add(cb.equal(studentScoresDailyRoot.get("readingLevel"), searchRequest.getReadingLevel()));

		if (!StringUtils.isEmpty(searchRequest.getScoreYearMonth()))
			filterPredicates.add(cb.equal(studentScoresDailyRoot.get("scoreYearMonth"), searchRequest.getScoreYearMonth()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(studentScoresDailyRoot.get(CommonConstants.SCORE_YEAR_MONTH)), cb.desc(studentScoresDailyRoot.get(CommonConstants.QUIZ_CORRECT)));
		
		// Build query
		TypedQuery<StudentScoresMonthlyTotal> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<StudentScoresMonthlyTotal> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, studentScoresDailyRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
