package com.enewschamp.app.admin.promotion.repository;

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

import com.enewschamp.domain.repository.RepositoryImpl;
@Repository
public class PromotionRepositoryCustomImpl extends RepositoryImpl implements PromotionRepositoryCustom{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Promotion> findPromotions(Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Promotion> criteriaQuery = cb.createQuery(Promotion.class);
		Root<Promotion> stateRoot = criteriaQuery.from(Promotion.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		// Build query
		TypedQuery<Promotion> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<Promotion> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, stateRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
