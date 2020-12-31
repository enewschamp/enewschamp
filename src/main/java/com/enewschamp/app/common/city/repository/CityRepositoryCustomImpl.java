package com.enewschamp.app.common.city.repository;

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
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.domain.repository.RepositoryImpl;
@Repository
public class CityRepositoryCustomImpl extends RepositoryImpl implements CityRepositoryCustom{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<City> findCities(AdminSearchRequest searchRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<City> criteriaQuery = cb.createQuery(City.class);
		Root<City> stateRoot = criteriaQuery.from(City.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		filterPredicates.add(cb.equal(stateRoot.get("countryId"), searchRequest.getCountryId()));
		filterPredicates.add(cb.equal(stateRoot.get("stateId"), searchRequest.getStateId()));
		filterPredicates.add(cb.like(stateRoot.get("nameId"), "%" + searchRequest.getName() + "%"));
		filterPredicates.add(cb.equal(stateRoot.get("isApplicableForNewsEvents"), searchRequest.getNewsEventsApplicable()));
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		// Build query
		TypedQuery<City> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<City> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, stateRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
