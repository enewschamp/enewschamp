package com.enewschamp.app.admin.student.registration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.student.registration.entity.StudentRegistration;

public interface StudentRegistrationRepositoryCustom {
	public Page<StudentRegistration> findStudentRegistrations(Pageable pageable, AdminSearchRequest searchRequest);
}
