package com.enewschamp.app.admin.student.scores.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;

public interface StudentScoresDailyRepositoryCustom {
	public Page<StudentScoresDaily> findStudentDailyScores(Pageable pageable, AdminSearchRequest searchRequest);
}