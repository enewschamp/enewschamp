package com.enewschamp.app.admin.schoolreport.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.admin.schoolreport.entity.SchoolReport;

@JaversSpringDataAuditable
public interface SchoolReportRepository extends JpaRepository<SchoolReport, Long> {
}
