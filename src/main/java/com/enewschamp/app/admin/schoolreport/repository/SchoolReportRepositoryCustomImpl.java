package com.enewschamp.app.admin.schoolreport.repository;

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
import com.enewschamp.app.admin.schoolreport.entity.SchoolReport;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.repository.GenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class SchoolReportRepositoryCustomImpl extends RepositoryImpl implements GenericListRepository<SchoolReport> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<SchoolReport> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SchoolReport> criteriaQuery = cb.createQuery(SchoolReport.class);
		Root<SchoolReport> schoolReportRoot = criteriaQuery.from(SchoolReport.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getStakeholderId()))
			filterPredicates.add(cb.equal(schoolReportRoot.get("stakeholderId"), searchRequest.getStakeholderId()));

		if (!StringUtils.isEmpty(searchRequest.getSchoolId()))
			filterPredicates.add(cb.equal(schoolReportRoot.get("schoolId"), searchRequest.getSchoolId()));

		if (!StringUtils.isEmpty(searchRequest.getEditionId()))
			filterPredicates.add(cb.equal(schoolReportRoot.get("editionId"), searchRequest.getEditionId()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(schoolReportRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<SchoolReport> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<SchoolReport> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, schoolReportRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
