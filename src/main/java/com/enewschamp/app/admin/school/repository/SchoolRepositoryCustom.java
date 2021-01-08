package com.enewschamp.app.admin.school.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.school.entity.School;

public interface SchoolRepositoryCustom {
	public Page<School> findSchools(Pageable pageable, AdminSearchRequest searchRequest);
}
