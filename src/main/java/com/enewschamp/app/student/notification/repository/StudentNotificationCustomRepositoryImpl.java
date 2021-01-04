package com.enewschamp.app.student.notification.repository;

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

import com.enewschamp.app.notification.Notification;
import com.enewschamp.app.notification.page.data.NotificationsSearchRequest;
import com.enewschamp.app.student.notification.ReadFlag;
import com.enewschamp.app.student.notification.StudentNotification;
import com.enewschamp.app.student.notification.StudentNotificationDTO;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.RepositoryImpl;

@Repository
public class StudentNotificationCustomRepositoryImpl extends RepositoryImpl
		implements StudentNotificationCustomRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<StudentNotificationDTO> getNotificationList(NotificationsSearchRequest searchRequest,
			Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<StudentNotificationDTO> criteriaQuery = cb.createQuery(StudentNotificationDTO.class);
		Root<StudentNotification> notificationRoot = criteriaQuery.from(StudentNotification.class);
		Root<Notification> notification = criteriaQuery.from(Notification.class);
		criteriaQuery = criteriaQuery.select(cb.construct(StudentNotificationDTO.class,
				notificationRoot.get("studentNotificationId"), notificationRoot.get("studentId"),
				notificationRoot.get("editionId"), notificationRoot.get("notificationId"),
				notification.get("notificationText"), notificationRoot.get("notificationDate"),
				notificationRoot.get("readFlag"), notificationRoot.get("readDateTime")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();
		filterPredicates.add(cb.equal(notificationRoot.get("notificationId"), notification.get("notificationId")));
		filterPredicates.add(cb.equal(notificationRoot.get("studentId"), searchRequest.getStudentId()));
		filterPredicates.add(cb.equal(notificationRoot.get("editionId"), searchRequest.getEditionId()));
		filterPredicates.add(cb.greaterThanOrEqualTo(notificationRoot.get("notificationDate"),
				searchRequest.getOperationDateTime()));
		filterPredicates.add(cb.equal(notificationRoot.get("recordInUse"), RecordInUseType.Y));
		if (searchRequest.getIsRead() != null && !"".equals(searchRequest.getIsRead())) {
			if ("Y".equals(searchRequest.getIsRead())) {
				filterPredicates.add(cb.equal(notificationRoot.get("readFlag"), ReadFlag.Y));
			} else {
				filterPredicates.add(cb.equal(notificationRoot.get("readFlag"), ReadFlag.N));
			}
		}

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(notificationRoot.get("notificationDate")));

		// Build query
		TypedQuery<StudentNotificationDTO> q = entityManager.createQuery(criteriaQuery);

		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());

		}
		List<StudentNotificationDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, notificationRoot);

		return new PageImpl<>(list, pageable, count);
	}

}
