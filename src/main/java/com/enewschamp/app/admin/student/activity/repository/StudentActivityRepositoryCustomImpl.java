package com.enewschamp.app.admin.student.activity.repository;

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
import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class StudentActivityRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<StudentActivity>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<StudentActivity> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<StudentActivity> criteriaQuery = cb.createQuery(StudentActivity.class);
		Root<StudentActivity> studentActivityRoot = criteriaQuery.from(StudentActivity.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentActivityRoot.get(STUDENT_ID), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getEditionId()))
			filterPredicates.add(cb.equal(studentActivityRoot.get(EDITION_ID), searchRequest.getEditionId()));

		if (!StringUtils.isEmpty(searchRequest.getNewsArticleId()))
			filterPredicates.add(cb.equal(studentActivityRoot.get(NEWS_ARTICLE_ID), searchRequest.getNewsArticleId()));

		if (!StringUtils.isEmpty(searchRequest.getReadingLevel()))
			filterPredicates.add(cb.equal(studentActivityRoot.get(READING_LEVEL), searchRequest.getReadingLevel()));

		if (!StringUtils.isEmpty(searchRequest.getReaction()))
			filterPredicates.add(cb.equal(studentActivityRoot.get(REACTION), searchRequest.getReaction()));

		if (!StringUtils.isEmpty(searchRequest.getSaved()))
			filterPredicates.add(cb.equal(studentActivityRoot.get(SAVED), searchRequest.getSaved()));

		if (!StringUtils.isEmpty(searchRequest.getOpinionPresent()) && searchRequest.getOpinionPresent().equals("Y")) {
			filterPredicates.add(cb.and(cb.isNotNull(studentActivityRoot.get(OPINIOIN)),
					cb.notEqual(cb.trim(studentActivityRoot.get(OPINIOIN)), "")));
		}
		if (!StringUtils.isEmpty(searchRequest.getOpinionPresent()) && searchRequest.getOpinionPresent().equals("N")) {
			filterPredicates.add(cb.or(cb.isNull(studentActivityRoot.get(OPINIOIN)),
					cb.equal(cb.trim(studentActivityRoot.get(OPINIOIN)), "")));
		}

		if (!StringUtils.isEmpty(searchRequest.getQuizScore()))
			filterPredicates.add(cb.equal(studentActivityRoot.get(QUIZ_SCORE), searchRequest.getQuizScore()));

		if (!StringUtils.isEmpty(searchRequest.getLastUpdateDateFrom()) && !StringUtils.isEmpty(searchRequest.getLastUpdateDateTo()))
			filterPredicates.add(cb.between(studentActivityRoot.get(CommonConstants.OPERATION_DATE_TIME),
					searchRequest.getLastUpdateDateFrom(), searchRequest.getLastUpdateDateTo()));
		
		if (!StringUtils.isEmpty(searchRequest.getQuizAttempted()) && searchRequest.getQuizAttempted().equals("Y"))
			filterPredicates.add(cb.and(cb.isNotNull(studentActivityRoot.get(QUIZ_SCORE)),
					cb.notEqual(cb.trim(studentActivityRoot.get(QUIZ_SCORE)), "")));
		
		if (!StringUtils.isEmpty(searchRequest.getQuizAttempted()) && searchRequest.getQuizAttempted().equals("N"))
			filterPredicates.add(cb.or(cb.isNull(studentActivityRoot.get(QUIZ_SCORE)),
					cb.equal(cb.trim(studentActivityRoot.get(QUIZ_SCORE)), "")));


		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(studentActivityRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<StudentActivity> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<StudentActivity> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, studentActivityRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
