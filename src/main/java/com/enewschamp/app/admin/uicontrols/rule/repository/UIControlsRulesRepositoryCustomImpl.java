package com.enewschamp.app.admin.uicontrols.rule.repository;

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
import com.enewschamp.app.common.uicontrols.entity.UIControlsRules;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class UIControlsRulesRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<UIControlsRules>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<UIControlsRules> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UIControlsRules> criteriaQuery = cb.createQuery(UIControlsRules.class);
		Root<UIControlsRules> uiControlsRulesRoot = criteriaQuery.from(UIControlsRules.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getRuleId()))
			filterPredicates.add(cb.equal(uiControlsRulesRoot.get(RULE_ID), searchRequest.getRuleId()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(uiControlsRulesRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<UIControlsRules> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<UIControlsRules> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, uiControlsRulesRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
