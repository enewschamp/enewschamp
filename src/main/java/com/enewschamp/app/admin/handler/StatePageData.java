package com.enewschamp.app.admin.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StatePageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private Long stateId;
	@JsonInclude
	@NotNull(message = MessageConstants.COUNTRY_NOT_NULL)
	@NotEmpty(message = MessageConstants.COUNTRY_NOT_EMPTY)
	private String countryId;
	@JsonInclude
	@NotNull(message = MessageConstants.STATE_NOT_NULL)
	@NotEmpty(message = MessageConstants.STATE_NOT_EMPTY)
	private String nameId;
	@JsonInclude
	@NotNull(message = MessageConstants.DESCRIPTION_NOT_NULL)
	@NotEmpty(message = MessageConstants.DESCRIPTION_NOT_EMPTY)
	private String description;
}

