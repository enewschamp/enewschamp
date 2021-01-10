package com.enewschamp.app.admin.student.subscription.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.subscription.domain.entity.StudentSubscription;

public interface StudentSubscriptionRepositoryCustom {
	public Page<StudentSubscription> findStudentSubscriptions(Pageable pageable, AdminSearchRequest searchRequest);
}
