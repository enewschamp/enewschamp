package com.enewschamp.subscription.app.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentSchoolPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<CountryPageData> countryLOV;
	private List<StatePageData> stateLOV;
	private List<CityPageData> cityLOV;
	private List<SchoolData> schoolLOV;
	private List<SchoolProgramLOV> schoolProgramLOV;
	private Long studentId = 0L;
	private String country;
	private String countryNotInTheList;
	private String state;
	private String stateNotInTheList;
	private String city;
	private String cityNotInTheList;
	private String school;
	private String schoolNotInTheList;
	private String section;
	private String grade;
	private String schoolProgramCode;
	private String approvalRequired;
}
