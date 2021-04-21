package com.enewschamp.app.admin.schoolsubscription.repository;

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
import com.enewschamp.app.admin.schoolsubscription.entity.SchoolSubscriptionGrade;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.repository.IGenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class SchoolSubscriptionGradeRepositoryCustomImpl extends RepositoryImpl
		implements IGenericListRepository<SchoolSubscriptionGrade>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<SchoolSubscriptionGrade> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SchoolSubscriptionGrade> criteriaQuery = cb.createQuery(SchoolSubscriptionGrade.class);
		Root<SchoolSubscriptionGrade> schoolSubscriptionGradeRoot = criteriaQuery.from(SchoolSubscriptionGrade.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getSchoolId()))
			filterPredicates.add(cb.equal(schoolSubscriptionGradeRoot.get(SCHOOL_ID), searchRequest.getSchoolId()));

		if (!StringUtils.isEmpty(searchRequest.getEditionId()))
			filterPredicates.add(cb.equal(schoolSubscriptionGradeRoot.get(EDITION_ID), searchRequest.getEditionId()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(schoolSubscriptionGradeRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<SchoolSubscriptionGrade> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<SchoolSubscriptionGrade> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, schoolSubscriptionGradeRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
