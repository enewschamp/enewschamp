package com.enewschamp.app.admin.user.leave.repository;

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
import com.enewschamp.user.domain.entity.UserLeave;

@Repository
public class UserLeaveRepositoryCustomImpl extends RepositoryImpl implements UserLeaveRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<UserLeave> findUserLeaves(AdminSearchRequest searchRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserLeave> criteriaQuery = cb.createQuery(UserLeave.class);
		Root<UserLeave> userLeaveRoot = criteriaQuery.from(UserLeave.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getUserId()))
			filterPredicates.add(cb.equal(userLeaveRoot.get("userId"), searchRequest.getUserId()));

		if (!StringUtils.isEmpty(searchRequest.getApprovalStatus()))
			filterPredicates.add(cb.equal(userLeaveRoot.get("approvalStatus"), searchRequest.getApprovalStatus()));

		if (!StringUtils.isEmpty(searchRequest.getStartDateFrom())
				&& !StringUtils.isEmpty(searchRequest.getStartDateTo()))
			filterPredicates.add(cb.between(userLeaveRoot.get("startDate"), searchRequest.getStartDateFrom(),
					searchRequest.getStartDateTo()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(userLeaveRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<UserLeave> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<UserLeave> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, userLeaveRoot);
		return new PageImpl<>(list, pageable, count);
	}

}