package com.enewschamp.app.admin.student.preference.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.subscription.domain.entity.StudentPreferences;

public interface StudentPreferencesRepositoryCustom {
	public Page<StudentPreferences> findStudentPreferences(AdminSearchRequest searchRequest, Pageable pageable);
}
