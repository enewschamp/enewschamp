package com.enewschamp.app.admin.student.details.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.subscription.domain.entity.StudentDetails;

public interface StudentDetailsRepositoryCustom {
	public Page<StudentDetails> findStudentDetails(Pageable pageable, AdminSearchRequest searchRequest);
}
