package com.enewschamp.app.admin.school.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SchoolPageData extends PageData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long schoolId;
	private Long schoolChainId;
	private String schoolProgram;
	private String schoolProgramCode;

	@NotNull(message = MessageConstants.STATE_NOT_NULL)
	@NotEmpty(message = MessageConstants.STATE_NOT_EMPTY)
	private String stateId;

	@NotNull(message = MessageConstants.COUNTRY_NOT_NULL)
	@NotEmpty(message = MessageConstants.COUNTRY_NOT_EMPTY)
	private String countryId;

	@NotNull(message = MessageConstants.CITY_NOT_NULL)
	@NotEmpty(message = MessageConstants.CITY_NOT_EMPTY)
	private String cityId;

	@NotNull(message = MessageConstants.SCHOOL_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.SCHOOL_NAME_NOT_EMPTY)
	private String name;

	private String website;
	private String ownership;
	private String eduBoard;
	private String eduMedium;
	private String genderDiversity;
	private String studentResidences;
	private String shiftDetails;
	private String feeStructure;
	@NotNull(message = MessageConstants.COMMENTS_NOT_NULL)
	@NotEmpty(message = MessageConstants.COMMENTS_NOT_EMPTY)
	private String comments;

}
