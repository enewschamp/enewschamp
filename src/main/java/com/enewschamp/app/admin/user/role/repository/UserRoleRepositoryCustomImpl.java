package com.enewschamp.app.admin.user.role.repository;

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
import com.enewschamp.user.domain.entity.UserRole;

@Repository
public class UserRoleRepositoryCustomImpl extends RepositoryImpl implements IGenericListRepository<UserRole> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<UserRole> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserRole> criteriaQuery = cb.createQuery(UserRole.class);
		Root<UserRole> studentachievementRoot = criteriaQuery.from(UserRole.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getUserId()))
			filterPredicates.add(cb.equal(studentachievementRoot.get("userId"), searchRequest.getUserId()));

		if (!StringUtils.isEmpty(searchRequest.getRoleId()))
			filterPredicates.add(cb.equal(studentachievementRoot.get("roleId"), searchRequest.getRoleId()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(studentachievementRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<UserRole> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<UserRole> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, studentachievementRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
