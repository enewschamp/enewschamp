package com.enewschamp.publication.domain.service;

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
import com.enewschamp.app.common.repository.IGenericListRepository;
import com.enewschamp.domain.repository.RepositoryImpl;
import com.enewschamp.publication.domain.entity.Avatar;
@Repository
public class AvatarRepositoryCustomImpl extends RepositoryImpl implements IGenericListRepository<Avatar>{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Avatar> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Avatar> criteriaQuery = cb.createQuery(Avatar.class);
		Root<Avatar> avatarRoot = criteriaQuery.from(Avatar.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(avatarRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<Avatar> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<Avatar> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, avatarRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
