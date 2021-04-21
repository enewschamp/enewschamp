package com.enewschamp.app.admin.edition.repository;

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
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.repository.IGenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;
import com.enewschamp.publication.domain.entity.Edition;

@Repository
public class EditionRepositoryCustomImpl extends RepositoryImpl implements IGenericListRepository<Edition> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Edition> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Edition> criteriaQuery = cb.createQuery(Edition.class);
		Root<Edition> editionRoot = criteriaQuery.from(Edition.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(editionRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<Edition> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<Edition> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, editionRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
