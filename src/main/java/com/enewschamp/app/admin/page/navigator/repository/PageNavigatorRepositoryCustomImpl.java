package com.enewschamp.app.admin.page.navigator.repository;

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
import com.enewschamp.app.fw.page.navigation.entity.PageNavigator;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class PageNavigatorRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<PageNavigator>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<PageNavigator> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PageNavigator> criteriaQuery = cb.createQuery(PageNavigator.class);
		Root<PageNavigator> pageNavigatorRoot = criteriaQuery.from(PageNavigator.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getNavId()))
			filterPredicates.add(cb.equal(pageNavigatorRoot.get(NAV_ID), searchRequest.getNavId()));

		if (!StringUtils.isEmpty(searchRequest.getCurrentPage()))
			filterPredicates.add(cb.equal(pageNavigatorRoot.get(CURRENT_PAGE), searchRequest.getCurrentPage()));

		if (!StringUtils.isEmpty(searchRequest.getOperation()))
			filterPredicates.add(cb.equal(pageNavigatorRoot.get(OPERATION), searchRequest.getOperation()));

		if (!StringUtils.isEmpty(searchRequest.getAction()))
			filterPredicates.add(cb.equal(pageNavigatorRoot.get(ACTION), searchRequest.getAction()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(pageNavigatorRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<PageNavigator> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<PageNavigator> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, pageNavigatorRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
