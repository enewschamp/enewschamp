package com.enewschamp.app.admin.stakeholder.repository;

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
import com.enewschamp.app.admin.stakeholder.entity.StakeHolder;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class StakeHolderRepositoryCustomImpl extends RepositoryImpl
		implements StakeHolderRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<StakeHolder> findStakeHolders(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<StakeHolder> criteriaQuery = cb.createQuery(StakeHolder.class);
		Root<StakeHolder> stakeHolderRoot = criteriaQuery.from(StakeHolder.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(searchRequest.getCategoryId()))
			filterPredicates.add(cb.like(stakeHolderRoot.get("name"), "%" + searchRequest.getName() + "%"));

		if (!StringUtils.isEmpty(searchRequest.getCategoryId()))
			filterPredicates.add(cb.like(stakeHolderRoot.get("surname"), "%" + searchRequest.getSurname() + "%"));


		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		// Build query
		TypedQuery<StakeHolder> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<StakeHolder> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, stakeHolderRoot);
		return new PageImpl<>(list, pageable, count);
	}

}