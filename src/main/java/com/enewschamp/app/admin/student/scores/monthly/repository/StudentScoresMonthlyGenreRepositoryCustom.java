package com.enewschamp.app.admin.student.scores.monthly.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;

public interface StudentScoresMonthlyGenreRepositoryCustom {
	public Page<StudentScoresMonthlyGenre> findStudentMonthlyScoresGenres(Pageable pageable, AdminSearchRequest searchRequest);
}