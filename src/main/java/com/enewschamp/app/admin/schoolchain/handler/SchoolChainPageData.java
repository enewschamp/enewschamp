package com.enewschamp.app.admin.schoolchain.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SchoolChainPageData extends PageData {

	private static final long serialVersionUID = 1L;

	private Long schoolChainId;

	@NotNull(message = MessageConstants.SCHOOL_CHAIN_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.SCHOOL_CHAIN_NAME_NOT_EMPTY)
	private String name;

	@NotNull(message = MessageConstants.COUNTRY_NOT_NULL)
	@NotEmpty(message = MessageConstants.COUNTRY_NOT_EMPTY)
	private String countryId;

	@NotNull(message = MessageConstants.PRESENCE_NOT_NULL)
	@NotEmpty(message = MessageConstants.PRESENCE_NOT_EMPTY)
	private String presence;

	private String stateId;
	private String cityId;
	private String eduBoard;
	private String eduMedium;
	private String genderDiversity;
	private String feeStructure;
	private String ownership;
	private String schoolProgram;
	private String shiftDetails;
	private String studentResidences;
	private String website;

	@NotNull(message = MessageConstants.COMMENTS_NOT_NULL)
	@NotEmpty(message = MessageConstants.COMMENTS_NOT_EMPTY)
	private String comments;

	private String operator;
	private LocalDateTime lastUpdate;

}
