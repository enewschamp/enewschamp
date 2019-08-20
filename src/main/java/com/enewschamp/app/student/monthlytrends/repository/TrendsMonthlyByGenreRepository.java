package com.enewschamp.app.student.monthlytrends.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.student.dailytrends.entity.TrendsDaily;
import com.enewschamp.app.student.monthlytrends.entity.TrendsMonthlyByGenre;

public interface TrendsMonthlyByGenreRepository extends JpaRepository<TrendsMonthlyByGenre, Long>{

	@Query("select d from TrendsMonthlyByGenre d where d.studentId = :studentId and d.editionId= :editionId and d.yearMonth= :yearMonth and d.genreId= :genreId and d.recordInUse ='Y'")
	public Optional<TrendsMonthlyByGenre> getMonghlyTrendsByGenre(@Param("studentId") Long studentId, @Param("editionId") String editionId, @Param("yearMonth")  Long yearMonth, @Param("genreId") String genreId);
}
