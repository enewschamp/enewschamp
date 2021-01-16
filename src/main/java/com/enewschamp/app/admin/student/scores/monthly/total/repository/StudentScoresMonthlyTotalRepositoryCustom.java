package com.enewschamp.app.admin.student.scores.monthly.total.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;

public interface StudentScoresMonthlyTotalRepositoryCustom {
	public Page<StudentScoresMonthlyTotal> findStudentMonthlyScoresTotals(Pageable pageable, AdminSearchRequest searchRequest);
}