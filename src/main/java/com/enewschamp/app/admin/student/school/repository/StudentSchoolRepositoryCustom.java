package com.enewschamp.app.admin.student.school.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.subscription.domain.entity.StudentSchool;

public interface StudentSchoolRepositoryCustom {
	public Page<StudentSchool> findStudentSchools(Pageable pageable, AdminSearchRequest searchRequest);
}
