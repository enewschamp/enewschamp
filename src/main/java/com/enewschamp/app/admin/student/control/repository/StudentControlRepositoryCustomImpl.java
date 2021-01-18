package com.enewschamp.app.admin.student.control.repository;

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

import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.domain.repository.RepositoryImpl;
import com.enewschamp.subscription.domain.entity.StudentControl;

@Repository
public class StudentControlRepositoryCustomImpl extends RepositoryImpl implements StudentControlRepositoryCustom{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<StudentControl> findStudentControls(Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<StudentControl> criteriaQuery = cb.createQuery(StudentControl.class);
		Root<StudentControl> studentControlRoot = criteriaQuery.from(StudentControl.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(studentControlRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<StudentControl> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<StudentControl> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, studentControlRoot);
		return new PageImpl<>(list, pageable, count);
	}

}
