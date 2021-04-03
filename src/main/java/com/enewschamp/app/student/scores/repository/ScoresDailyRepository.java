package com.enewschamp.app.student.scores.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.student.scores.entity.ScoresDaily;

public interface ScoresDailyRepository extends JpaRepository<ScoresDaily, Long> {

	@Query("select d from ScoresDaily d where d.studentId = :studentId and d.editionId= :editionId and d.readingLevel= :readingLevel and d.publicationDate= :publicationDate and d.recordInUse ='Y'")
	public Optional<ScoresDaily> getScoresDaily(@Param("studentId") Long studentId,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel,
			@Param("publicationDate") LocalDate publicationDate);
}