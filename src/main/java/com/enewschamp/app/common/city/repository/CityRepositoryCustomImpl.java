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
import org.springframework.util.StringUtils;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.app.common.repository.IGenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class CityRepositoryCustomImpl extends RepositoryImpl implements IGenericListRepository<City> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<City> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<City> criteriaQuery = cb.createQuery(City.class);
		Root<City> cityRoot = criteriaQuery.from(City.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		if (!StringUtils.isEmpty(searchRequest.getCountryId()))
			filterPredicates.add(cb.equal(cityRoot.get("countryId"), searchRequest.getCountryId()));
		if (!StringUtils.isEmpty(searchRequest.getStateId()))
			filterPredicates.add(cb.equal(cityRoot.get("stateId"), searchRequest.getStateId()));
		if (!StringUtils.isEmpty(searchRequest.getName()))
			filterPredicates.add(cb.like(cityRoot.get("nameId"), "%" + searchRequest.getName() + "%"));
		if (!StringUtils.isEmpty(searchRequest.getNewsEventsApplicable()))
			filterPredicates
					.add(cb.equal(cityRoot.get("isApplicableForNewsEvents"), searchRequest.getNewsEventsApplicable()));
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(cityRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<City> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<City> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, cityRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
