package com.enewschamp.app.common.state.repository;

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

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.common.state.entity.State;
import com.enewschamp.domain.repository.RepositoryImpl;
@Repository
public class StateRepositoryCustomImpl extends RepositoryImpl implements StateRepositoryCustom{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<State> findStates(AdminSearchRequest searchRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<State> criteriaQuery = cb.createQuery(State.class);
		Root<State> stateRoot = criteriaQuery.from(State.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		filterPredicates.add(cb.like(stateRoot.get("countryId"), "%" + searchRequest.getCountryId() + "%"));
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		// Build query
		TypedQuery<State> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<State> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, stateRoot);
		return new PageImpl<>(list, pageable, count);
	}

}