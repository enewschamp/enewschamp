package com.enewschamp.app.admin.schoolpricing.repository;

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
import com.enewschamp.app.school.entity.SchoolPricing;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class SchoolPricingRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<SchoolPricing>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<SchoolPricing> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SchoolPricing> criteriaQuery = cb.createQuery(SchoolPricing.class);
		Root<SchoolPricing> schoolPricingRoot = criteriaQuery.from(SchoolPricing.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getInstitutionId()))
			filterPredicates.add(cb.equal(schoolPricingRoot.get(INSTITUTION_ID), searchRequest.getInstitutionId()));

		if (!StringUtils.isEmpty(searchRequest.getInstitutionType()))
			filterPredicates.add(cb.equal(schoolPricingRoot.get(INSTITUTION_TYPE), searchRequest.getInstitutionType()));

		if (!StringUtils.isEmpty(searchRequest.getEffectiveDateFrom())
				&& !StringUtils.isEmpty(searchRequest.getEffectiveDateTo()))
			filterPredicates.add(cb.between(schoolPricingRoot.get(EFFECTIVE_DATE), searchRequest.getEffectiveDateFrom(),
					searchRequest.getEffectiveDateTo()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(schoolPricingRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<SchoolPricing> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<SchoolPricing> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, schoolPricingRoot);
		return new PageImpl<>(list, pageable, count);
	}

}