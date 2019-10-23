package com.enewschamp.subscription.app.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentSchoolPageData  extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private long studentID = 0L;
	
	private long schoolId = 0L;
	
	private String grade;
	private SchoolData school;
	private String section;
	private String emailId;
	
	private CountryPageData country;
	private StatePageData state;
	private String cityID;
	
	private List<CountryPageData> countryLOV;
	private String incompeleteFormText;
	
}
