package com.enewschamp.app.school.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.subscription.app.dto.SchoolProgramLOV;

public interface SchoolProgramRepository extends JpaRepository<SchoolProgramLOV, Long> {

	@Query("Select new SchoolProgramLOV(s.schoolId as id,s.name as name,s.schoolProgramCode as schoolProgramCode,c.nameId as city,"
			+ "c.description as cityName,st.nameId as state,st.description as stateName,ct.nameId as country,ct.description as countryName) "
			+ "from School s,City c,State st,Country ct"
			+ " where s.schoolProgram='Y' and s.recordInUse='Y' and s.cityId=c.nameId and s.stateId=st.nameId and s.countryId=ct.nameId")
	public List<SchoolProgramLOV> getSchoolsForProgramCode();

}
