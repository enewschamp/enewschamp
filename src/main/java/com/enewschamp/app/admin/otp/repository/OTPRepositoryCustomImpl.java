package com.enewschamp.app.admin.otp.repository;

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
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.repository.IGenericListRepository;
import com.enewschamp.app.otp.entity.OTP;
import com.enewschamp.domain.repository.RepositoryImpl;

@Repository
public class OTPRepositoryCustomImpl extends RepositoryImpl implements IGenericListRepository<OTP>, AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<OTP> findAll(Pageable pageable, AdminSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<OTP> criteriaQuery = cb.createQuery(OTP.class);
		Root<OTP> otpRoot = criteriaQuery.from(OTP.class);
		List<Predicate> filterPredicates = new ArrayList<>();

		if (!StringUtils.isEmpty(searchRequest.getEmailId()))
			filterPredicates.add(cb.like(otpRoot.get(EMAIL_ID), "%" + searchRequest.getEmailId() + "%"));

		if (!StringUtils.isEmpty(searchRequest.getPhoneNumber()))
			filterPredicates.add(cb.equal(otpRoot.get(PHONE_NUMBER), searchRequest.getPhoneNumber()));

		if (!StringUtils.isEmpty(searchRequest.getVerified()))
			filterPredicates.add(cb.equal(otpRoot.get(VERIFIED), searchRequest.getVerified()));

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(otpRoot.get(CommonConstants.OPERATION_DATE_TIME)));
		// Build query
		TypedQuery<OTP> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<OTP> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, otpRoot);
		return new PageImpl<>(list, pageable, count);
	}

}