package com.enewschamp.app.admin.page.navigator.rule.repository;

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
import com.enewschamp.app.fw.page.navigation.entity.PageNavigatorRules;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class PageNavigatorRulesRepositoryCustomImpl extends RepositoryImpl
		implements GenericListRepository<PageNavigatorRules> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<PageNavigatorRules> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PageNavigatorRules> criteriaQuery = cb.createQuery(PageNavigatorRules.class);
		Root<PageNavigatorRules> pageNavigatorRoot = criteriaQuery.from(PageNavigatorRules.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getNavId()))
			filterPredicates.add(cb.equal(pageNavigatorRoot.get("navId"), searchRequest.getNavId()));

		if (!StringUtils.isEmpty(searchRequest.getRuleId()))
			filterPredicates.add(cb.equal(pageNavigatorRoot.get("ruleId"), searchRequest.getRuleId()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(pageNavigatorRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<PageNavigatorRules> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<PageNavigatorRules> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, pageNavigatorRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
