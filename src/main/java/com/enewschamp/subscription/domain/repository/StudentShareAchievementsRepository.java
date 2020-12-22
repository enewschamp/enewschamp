package com.enewschamp.subscription.domain.repository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentShareAchievements;

@JaversSpringDataAuditable
public interface StudentShareAchievementsRepository extends JpaRepository<StudentShareAchievements, Long> {

	@Query("Select a from StudentShareAchievements a where a.studentId= :studentId")
	public List<StudentShareAchievements> getStudentAchievements(@Param("studentId") Long studentId);
}
