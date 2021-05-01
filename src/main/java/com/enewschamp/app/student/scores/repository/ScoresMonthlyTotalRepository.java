package com.enewschamp.app.student.scores.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.student.scores.entity.ScoresMonthlyTotal;

public interface ScoresMonthlyTotalRepository extends JpaRepository<ScoresMonthlyTotal, Long> {

	@Query("select d from ScoresMonthlyTotal d where d.studentId = :studentId and d.editionId= :editionId and d.readingLevel= :readingLevel and d.yearMonth= :yearMonth and d.recordInUse ='Y'")
	public Optional<ScoresMonthlyTotal> getScoresMonthly(@Param("studentId") Long studentId,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel,
			@Param("yearMonth") Long yearMonth);
}
