package com.enewschamp.app.admin.student.school.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentSchoolPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private long studentId;

	@NotNull(message = MessageConstants.COUNTRY_NOT_NULL)
	@NotEmpty(message = MessageConstants.COUNTRY_NOT_EMPTY)
	private String country;

	private String countryNotInTheList;

	@NotNull(message = MessageConstants.STATE_NOT_NULL)
	@NotEmpty(message = MessageConstants.STATE_NOT_EMPTY)
	private String state;

	private String stateNotInTheList;

	@NotNull(message = MessageConstants.CITY_NOT_NULL)
	@NotEmpty(message = MessageConstants.CITY_NOT_EMPTY)
	private String city;

	private String cityNotInTheList;

	@NotNull(message = MessageConstants.SCHOOL_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.SCHOOL_NAME_NOT_EMPTY)
	private String school;

	private String schoolNotInTheList;
	private String section;
	private String grade;

}
