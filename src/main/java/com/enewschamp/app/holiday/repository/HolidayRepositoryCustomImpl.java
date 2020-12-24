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

import com.enewschamp.app.holiday.entity.Holiday;
import com.enewschamp.domain.repository.RepositoryImpl;
@Repository
public class HolidayRepositoryCustomImpl extends RepositoryImpl implements HolidayRepositoryCustom{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Holiday> findHolidays(Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Holiday> criteriaQuery = cb.createQuery(Holiday.class);
		Root<Holiday> stateRoot = criteriaQuery.from(Holiday.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		// Build query
		TypedQuery<Holiday> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<Holiday> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, stateRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
