package com.enewschamp.app.student.badges.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.student.badges.entity.StudentBadges;

public interface StudentBadgesRepository extends JpaRepository<StudentBadges, Long> {

	@Query("select d from StudentBadges d where d.studentId = :studentId and d.yearMonth= :yearMonth and d.recordInUse ='Y'")
	public Optional<StudentBadges> getStudentBadges(@Param("studentId") Long studentId,
			@Param("yearMonth") Long yearMonth);

}
