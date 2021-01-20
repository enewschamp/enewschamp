package com.enewschamp.app.admin.uicontrols.repository;

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
import com.enewschamp.app.common.uicontrols.entity.UIControls;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class UIControlsRepositoryCustomImpl extends RepositoryImpl implements IGenericListRepository<UIControls>{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<UIControls> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UIControls> criteriaQuery = cb.createQuery(UIControls.class);
		Root<UIControls> uiControlsRoot = criteriaQuery.from(UIControls.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(searchRequest.getUiControlId()))
			filterPredicates.add(cb.equal(uiControlsRoot.get("uiControlId"), searchRequest.getUiControlId()));

		if (!StringUtils.isEmpty(searchRequest.getPageName()))
			filterPredicates.add(cb.equal(uiControlsRoot.get("pageName"), searchRequest.getPageName()));
		

		if (!StringUtils.isEmpty(searchRequest.getOperation()))
			filterPredicates.add(cb.equal(uiControlsRoot.get("operation"), searchRequest.getOperation()));
		

		if (!StringUtils.isEmpty(searchRequest.getControlName()))
			filterPredicates.add(cb.equal(uiControlsRoot.get("controlName"), searchRequest.getControlName()));
		

		if (!StringUtils.isEmpty(searchRequest.getGlobalControlRef()))
			filterPredicates.add(cb.equal(uiControlsRoot.get("globalControlRef"), searchRequest.getGlobalControlRef()));

		if (!StringUtils.isEmpty(searchRequest.getIsPremiumFeature()))
			filterPredicates.add(cb.equal(uiControlsRoot.get("isPremiumFeature"), searchRequest.getIsPremiumFeature()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(uiControlsRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<UIControls> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<UIControls> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, uiControlsRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
