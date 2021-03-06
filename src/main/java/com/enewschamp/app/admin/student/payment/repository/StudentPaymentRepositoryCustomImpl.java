package com.enewschamp.app.admin.student.payment.repository;

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
import com.enewschamp.domain.repository.RepositoryImpl;
import com.enewschamp.subscription.domain.entity.StudentPayment;

@Repository
public class StudentPaymentRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<StudentPayment>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<StudentPayment> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<StudentPayment> criteriaQuery = cb.createQuery(StudentPayment.class);
		Root<StudentPayment> studentPaymentRoot = criteriaQuery.from(StudentPayment.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentPaymentRoot.get(STUDENT_ID), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getEditionId()))
			filterPredicates.add(cb.equal(studentPaymentRoot.get(EDITION_ID), searchRequest.getEditionId()));

		if (!StringUtils.isEmpty(searchRequest.getSubscriptionType()))
			filterPredicates
					.add(cb.equal(studentPaymentRoot.get(SUBSCRIPTION_TYPE), searchRequest.getSubscriptionType()));

		if (!StringUtils.isEmpty(searchRequest.getSubscriptionPeriod()))
			filterPredicates
					.add(cb.equal(studentPaymentRoot.get(SUBSCRIPTION_PERIOD), searchRequest.getSubscriptionPeriod()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(studentPaymentRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<StudentPayment> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<StudentPayment> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, studentPaymentRoot);
		return new PageImpl<>(list, pageable, count);
	}

}