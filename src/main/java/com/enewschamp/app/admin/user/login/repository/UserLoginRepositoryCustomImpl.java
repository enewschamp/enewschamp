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
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class UserLoginRepositoryCustomImpl extends RepositoryImpl implements IGenericListRepository<UserLogin>, AdminConstant{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<UserLogin> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserLogin> criteriaQuery = cb.createQuery(UserLogin.class);
		Root<UserLogin> studentachievementRoot = criteriaQuery.from(UserLogin.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getUserId()))
			filterPredicates.add(cb.equal(studentachievementRoot.get(USER_ID), searchRequest.getUserId()));

		if (!StringUtils.isEmpty(searchRequest.getUserType()))
			filterPredicates.add(cb.equal(studentachievementRoot.get(USER_TYPE), searchRequest.getUserType()));

		if (!StringUtils.isEmpty(searchRequest.getLoginFlag()))
			filterPredicates.add(cb.equal(studentachievementRoot.get(LOGIN_FLAG), searchRequest.getLoginFlag()));

		if (!StringUtils.isEmpty(searchRequest.getAppName()))
			filterPredicates.add(cb.equal(studentachievementRoot.get(APP_NAME), searchRequest.getAppName()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(studentachievementRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<UserLogin> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<UserLogin> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, studentachievementRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
