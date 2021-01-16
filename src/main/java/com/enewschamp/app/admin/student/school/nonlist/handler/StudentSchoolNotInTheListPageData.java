package com.enewschamp.app.admin.student.school.nonlist.handler;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.city.dto.CityDTO;
import com.enewschamp.app.common.state.dto.StateDTO;
import com.enewschamp.app.school.dto.SchoolDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentSchoolNotInTheListPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private StateDTO state;
	private CityDTO city;
	private SchoolDTO school;
	private StudentSchoolDTO studentSchool;
}