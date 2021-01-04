package com.enewschamp.app.school.repository;

import java.util.List;

import com.enewschamp.subscription.app.dto.SchoolProgramLOV;

public interface SchoolProgramRepository {

	public List<SchoolProgramLOV> getSchoolsForProgramCode();

}
