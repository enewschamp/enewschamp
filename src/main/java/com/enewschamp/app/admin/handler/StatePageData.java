package com.enewschamp.app.admin.handler;

import javax.validation.constraints.NotNull;

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
	@NotNull(message="Country id must not be null")
	private String countryId;
	@JsonInclude
	@NotNull(message="State name must not be null")
	private String nameId;
	@JsonInclude
	@NotNull(message="Description must not be null")
	private String description;
}

