package com.enewschamp.app.holiday.repository;

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
import com.enewschamp.app.common.repository.GenericListRepository;
import com.enewschamp.app.holiday.entity.Holiday;
import com.enewschamp.domain.repository.RepositoryImpl;
@Repository
public class HolidayRepositoryCustomImpl extends RepositoryImpl implements GenericListRepository<Holiday>{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Holiday> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Holiday> criteriaQuery = cb.createQuery(Holiday.class);
		Root<Holiday> holidayRoot = criteriaQuery.from(Holiday.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(holidayRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<Holiday> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<Holiday> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, holidayRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
