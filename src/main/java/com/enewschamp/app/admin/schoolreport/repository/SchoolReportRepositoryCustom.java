package com.enewschamp.app.admin.schoolreport.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.schoolreport.entity.SchoolReport;

public interface SchoolReportRepositoryCustom {
	public Page<SchoolReport> findSchoolReports(Pageable pageable, AdminSearchRequest searchRequest);
}
