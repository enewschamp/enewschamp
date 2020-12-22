package com.enewschamp.app.student.dailytrends.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.student.dailytrends.entity.TrendsDaily;

public interface TrendsDailyRepository extends JpaRepository<TrendsDaily, Long> {

	@Query("select d from TrendsDaily d where d.studentId = :studentId and d.editionId= :editionId and d.readingLevel= :readingLevel and d.quizPublicationDate= :quizPublicationDate and d.recordInUse ='Y'")
	public Optional<TrendsDaily> getDailyTrend(@Param("studentId") Long studentId, @Param("editionId") String editionId,
			@Param("readingLevel") int readingLevel, @Param("quizPublicationDate") LocalDate quizPublicationDate);
}
