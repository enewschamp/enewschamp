package com.enewschamp.app.admin.user.login.repository;

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
import com.enewschamp.app.user.login.entity.UserActivityTracker;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class UserActivityTrackerRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<UserActivityTracker>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<UserActivityTracker> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserActivityTracker> criteriaQuery = cb.createQuery(UserActivityTracker.class);
		Root<UserActivityTracker> userActivityTrackerRoot = criteriaQuery.from(UserActivityTracker.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getUserId()))
			filterPredicates.add(cb.like(userActivityTrackerRoot.get(USER_ID), "%" + searchRequest.getUserId() + "%"));

		if (!StringUtils.isEmpty(searchRequest.getDeviceId()))
			filterPredicates
					.add(cb.like(userActivityTrackerRoot.get(DEVICE_ID), "%" + searchRequest.getDeviceId() + "%"));

		if (!StringUtils.isEmpty(searchRequest.getActionPerformed()))
			filterPredicates.add(cb.like(userActivityTrackerRoot.get(ACTION_PERFORMED),
					"%" + searchRequest.getActionPerformed() + "%"));

		if (!StringUtils.isEmpty(searchRequest.getActionDateFrom())
				&& !StringUtils.isEmpty(searchRequest.getActionDateTo()))
			filterPredicates.add(cb.between(userActivityTrackerRoot.get(ACTION_TIME), searchRequest.getActionDateFrom(),
					searchRequest.getActionDateTo()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(userActivityTrackerRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<UserActivityTracker> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<UserActivityTracker> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, userActivityTrackerRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
