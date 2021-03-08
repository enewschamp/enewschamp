package com.enewschamp.app.holiday.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.holiday.entity.Holiday;
import com.enewschamp.publication.domain.common.HolidayList;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

	@Query("select h from Holiday h where h.holidayDate= :holidayDate and h.editionId=:editionId and h.recordInUse='Y'")
	public Optional<Holiday> getHoliday(@Param("holidayDate") LocalDate date, @Param("editionId") String editionId);

	@Query(value = "select a.holidayDate as holidayDate, a.holiday as holiday, a.publication as publication, a.helpdesk as helpdesk from Holiday a where YEAR(a.holidayDate) IN((YEAR(CURDATE())-1),YEAR(CURDATE()),(YEAR(CURDATE())+1))")
	public List<HolidayList> getHolidayList();
}
