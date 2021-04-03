package com.enewschamp.app.student.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.student.entity.StudentActivity;

public interface StudentActivityRepository extends JpaRepository<StudentActivity, Long> {

	@Query("select s from StudentActivity s where s.studentId = :studentId and s.newsArticleId = :newsArticleId and s.recordInUse = 'Y'")
	public Optional<StudentActivity> getStudentActivity(@Param("studentId") Long studentId,
			@Param("newsArticleId") Long newsArticleId);

	@Query("select s from StudentActivity s where s.studentId = :studentId and s.saved = 'Y' and s.recordInUse = 'Y'")
	public List<StudentActivity> getSavedArticles(@Param("studentId") Long studentId);

}
