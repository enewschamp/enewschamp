package com.enewschamp.app.admin.student.subscription.repository;

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
import com.enewschamp.app.common.repository.IGenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;
import com.enewschamp.subscription.domain.entity.StudentSubscription;

@Repository
public class StudentSubscriptionRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<StudentSubscription> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<StudentSubscription> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<StudentSubscription> criteriaQuery = cb.createQuery(StudentSubscription.class);
		Root<StudentSubscription> studentSubscriptionRoot = criteriaQuery.from(StudentSubscription.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentSubscriptionRoot.get("studentId"), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getSubscriptionType()))
			filterPredicates.add(
					cb.equal(studentSubscriptionRoot.get("subscriptionType"), searchRequest.getSubscriptionType()));

		if (!StringUtils.isEmpty(searchRequest.getAutoRenewal()))
			filterPredicates.add(cb.equal(studentSubscriptionRoot.get("autoRenewal"), searchRequest.getAutoRenewal()));

		if (!StringUtils.isEmpty(searchRequest.getStartDateFrom())
				&& !StringUtils.isEmpty(searchRequest.getStartDateTo()))
			filterPredicates.add(cb.between(studentSubscriptionRoot.get("startDate"), searchRequest.getStartDateFrom(),
					searchRequest.getStartDateTo()));

		if (!StringUtils.isEmpty(searchRequest.getEndDateFrom()) && !StringUtils.isEmpty(searchRequest.getEndDateTo()))
			filterPredicates.add(cb.between(studentSubscriptionRoot.get("endDate"), searchRequest.getEndDateFrom(),
					searchRequest.getEndDateTo()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(studentSubscriptionRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<StudentSubscription> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<StudentSubscription> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, studentSubscriptionRoot);
		return new PageImpl<>(list, pageable, count);
	}

}