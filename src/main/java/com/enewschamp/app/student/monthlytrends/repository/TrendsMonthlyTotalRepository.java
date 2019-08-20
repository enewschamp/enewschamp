package com.enewschamp.app.student.monthlytrends.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.student.monthlytrends.entity.TrendsMonthlyTotal;

public interface TrendsMonthlyTotalRepository extends JpaRepository<TrendsMonthlyTotal, Long>{

	@Query("select d from TrendsMonthlyTotal d where d.studentId = :studentId and d.editionId= :editionId and d.yearMonth= :yearMonth and d.recordInUse ='Y'")
	public Optional<TrendsMonthlyTotal> getMonthlyTrends(@Param("studentId") Long studentId, @Param("editionId") String editionId, @Param("yearMonth")  Long yearMonth);
}
