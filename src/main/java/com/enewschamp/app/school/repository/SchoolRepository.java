package com.enewschamp.app.school.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.school.entity.School;

public interface SchoolRepository extends JpaRepository<School, Long> {

	@Query("Select s from School s where s.cityId= :cityId and s.stateId= :stateId and s.countryId= :countryId and recordInUse='Y'")
	public List<School> getSchools(@Param("cityId") String cityId, @Param("stateId") String stateId,
			@Param("countryId") String countryId);

	@Query("Select s from School s where s.schoolProgramCode= :schoolProgramCode and recordInUse='Y'")
	public List<School> getSchoolFromProgramCode(@Param("schoolProgramCode") String schoolProgramCode);
}
