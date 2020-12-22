package com.enewschamp.app.holiday.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.holiday.entity.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

	@Query("select h from Holiday h where h.holidayDate= :holidayDate and h.editionId=:editionId and h.recordInUse='Y'")
	public Optional<Holiday> getHoliday(@Param("holidayDate") LocalDate date, @Param("editionId") String editionId);
}
