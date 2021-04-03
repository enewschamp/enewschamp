package com.enewschamp.app.admin.errorcode.repository;

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
import com.enewschamp.common.domain.entity.ErrorCodes;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class ErrorCodesRepositoryCustomImpl extends RepositoryImpl implements IGenericListRepository<ErrorCodes>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<ErrorCodes> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ErrorCodes> criteriaQuery = cb.createQuery(ErrorCodes.class);
		Root<ErrorCodes> errorCodesRoot = criteriaQuery.from(ErrorCodes.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getErrorCodeId()))
			filterPredicates.add(cb.equal(errorCodesRoot.get(ERROR_CODE_ID), searchRequest.getErrorCodeId()));

		if (!StringUtils.isEmpty(searchRequest.getErrorCategory()))
			filterPredicates.add(cb.equal(errorCodesRoot.get(ERROR_CATEGORY), searchRequest.getErrorCategory()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(errorCodesRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<ErrorCodes> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<ErrorCodes> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, errorCodesRoot);
		return new PageImpl<>(list, pageable, count);
	}

}