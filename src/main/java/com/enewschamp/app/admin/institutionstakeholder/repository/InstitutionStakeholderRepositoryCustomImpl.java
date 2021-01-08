package com.enewschamp.app.admin.institutionstakeholder.repository;

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
import com.enewschamp.app.admin.institutionstakeholder.entity.InstitutionStakeholder;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class InstitutionStakeholderRepositoryCustomImpl extends RepositoryImpl
		implements InstitutionStakeholderRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<InstitutionStakeholder> findInstitutionalStakeHolders(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<InstitutionStakeholder> criteriaQuery = cb.createQuery(InstitutionStakeholder.class);
		Root<InstitutionStakeholder> inststakeHolderRoot = criteriaQuery.from(InstitutionStakeholder.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(searchRequest.getInstitutionType()))
			filterPredicates.add(cb.equal(inststakeHolderRoot.get("institutionType"), searchRequest.getInstitutionType()));

		if (!StringUtils.isEmpty(searchRequest.getInstitutionId()))
			filterPredicates.add(cb.equal(inststakeHolderRoot.get("institutionId"), searchRequest.getInstitutionId()));

		if (!StringUtils.isEmpty(searchRequest.getStakeholderId()))
			filterPredicates.add(cb.equal(inststakeHolderRoot.get("stakeholderId"), searchRequest.getStakeholderId()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		// Build query
		TypedQuery<InstitutionStakeholder> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<InstitutionStakeholder> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, inststakeHolderRoot);
		return new PageImpl<>(list, pageable, count);
	}

}