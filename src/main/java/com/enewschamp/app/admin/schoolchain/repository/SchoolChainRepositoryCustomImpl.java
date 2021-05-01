package com.enewschamp.app.admin.schoolchain.repository;

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
import com.enewschamp.app.admin.schoolchain.entity.SchoolChain;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.repository.IGenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class SchoolChainRepositoryCustomImpl extends RepositoryImpl implements IGenericListRepository<SchoolChain> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<SchoolChain> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SchoolChain> criteriaQuery = cb.createQuery(SchoolChain.class);
		Root<SchoolChain> schoolChainRoot = criteriaQuery.from(SchoolChain.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(schoolChainRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<SchoolChain> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<SchoolChain> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, schoolChainRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
