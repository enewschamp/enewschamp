package com.enewschamp.app.admin.student.achievement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.subscription.domain.entity.StudentShareAchievements;

public interface StudentShareAchievementsRepositoryCustom {
	public Page<StudentShareAchievements> findStudentShareAchievements(Pageable pageable, AdminSearchRequest searchRequest);
}
