package com.enewschamp.app.admin.student.school.nonlist.handler;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
//@JsonIgnoreProperties({ "operatorId", "lastUpdate", "recordInUse"})
public class StudentSchoolNotInTheListPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private StateNilDTO state;
	private CityNilDTO city;
	private SchoolNilDTO school;
	private StudentSchoolNilDTO studentSchool;
}