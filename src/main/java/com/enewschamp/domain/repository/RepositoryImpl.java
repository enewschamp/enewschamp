package com.enewschamp.domain.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.query.criteria.internal.path.RootImpl;

public class RepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;

	protected Long getRecordCount(CriteriaQuery criteriaQuery,
							   	  List<Predicate> filterPredicates, 
							   	  Root originalRoot) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

		Root mainRoot = countQuery.from(originalRoot.getJavaType());

		if (criteriaQuery.getRoots() != null) {
			for (Object root : criteriaQuery.getRoots()) {
				RootImpl rootImpl = (RootImpl) root;
				if (!rootImpl.getJavaType().equals(mainRoot.getJavaType())) {
					countQuery.from(rootImpl.getJavaType());
				}
			}
		}
		countQuery.select(cb.count(mainRoot)).where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		return (Long) entityManager.createQuery(countQuery).getSingleResult();
	}

}
