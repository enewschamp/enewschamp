package com.enewschamp.app.admin.schoolpricing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.school.entity.SchoolPricing;

public interface SchoolPricingRepositoryCustom {
	public Page<SchoolPricing> findSchoolPricings(Pageable pageable, AdminSearchRequest searchRequest);
}
