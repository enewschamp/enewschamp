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
public class CountryPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private Long countryId;
	@JsonInclude
	private int isd;
	@JsonInclude
	@NotNull(message = MessageConstants.COUNTRY_NOT_NULL)
	@NotEmpty(message = MessageConstants.COUNTRY_NOT_EMPTY)
	private String nameId;
	@JsonInclude
	@NotNull(message = MessageConstants.DESCRIPTION_NOT_NULL)
	@NotEmpty(message = MessageConstants.DESCRIPTION_NOT_EMPTY)
	private String description;
	@JsonInclude
	private String currencyId;
	@JsonInclude
	private String language;
}
