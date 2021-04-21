package com.enewschamp.app.student.quiz.repository;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.student.quiz.entity.QuizScore;

public interface QuizScoreRepository extends JpaRepository<QuizScore, Long> {

	@Cacheable
	@Query("Select q from QuizScore q where q.studentId= :studentId and q.newsArticleQuizId= :articleQuizId and q.recordInUse= 'Y'")
	public Optional<QuizScore> getQuizScore(@Param("studentId") Long studentId, @Param("articleQuizId") Long articleId);

}
