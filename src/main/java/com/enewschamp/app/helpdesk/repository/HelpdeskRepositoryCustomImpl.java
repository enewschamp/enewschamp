package com.enewschamp.app.helpdesk.repository;

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
import com.enewschamp.app.helpdesk.entity.Helpdesk;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class HelpdeskRepositoryCustomImpl extends RepositoryImpl implements HelpdeskRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Helpdesk> findHelpDesks(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Helpdesk> criteriaQuery = cb.createQuery(Helpdesk.class);
		Root<Helpdesk> HelpdeskRoot = criteriaQuery.from(Helpdesk.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(HelpdeskRoot.get("studentId"), searchRequest.getStudentId()));
		
		if (!StringUtils.isEmpty(searchRequest.getCreateDateFrom()))
			filterPredicates.add(cb.between(HelpdeskRoot.get("createDateTime"), searchRequest.getCreateDateFrom(),
					searchRequest.getCreateDateTo()));
		
		if (!StringUtils.isEmpty(searchRequest.getCategoryId()))
			filterPredicates.add(cb.equal(HelpdeskRoot.get("categoryId"), searchRequest.getCategoryId()));
		
		if (!StringUtils.isEmpty(searchRequest.getCloseFlag()))
			filterPredicates.add(cb.equal(HelpdeskRoot.get("closeFlag"), searchRequest.getCloseFlag()));
		
		if (!StringUtils.isEmpty(searchRequest.getSupportUserId()))
			filterPredicates.add(cb.equal(HelpdeskRoot.get("operatorId"), searchRequest.getSupportUserId()));
		
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		// Build query
		TypedQuery<Helpdesk> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<Helpdesk> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, HelpdeskRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
