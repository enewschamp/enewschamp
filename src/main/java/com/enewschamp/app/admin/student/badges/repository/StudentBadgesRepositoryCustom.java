package com.enewschamp.app.admin.student.badges.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.student.badges.entity.StudentBadges;

public interface StudentBadgesRepositoryCustom {
	public Page<StudentBadges> findStudentBadges(Pageable pageable, AdminSearchRequest searchRequest);
}
