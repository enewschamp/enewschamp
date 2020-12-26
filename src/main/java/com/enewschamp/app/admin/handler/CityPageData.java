package com.enewschamp.app.admin.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CityPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private Long id;
	@JsonInclude
	@NotNull(message = "Country id must not be null")
	private String countryId;
	@JsonInclude
	@NotNull(message = "State id must not be null")
	private String stateId;
	@NotNull
	@JsonInclude
	@NotNull(message = "Name must not be null")
	private String name;
	@JsonInclude
	@NotNull(message = "Description must not be null")
	private String description;
//	@JsonInclude
//	private String recordInUse;
//	@JsonInclude
//	private String operator;
//	@JsonInclude
//	protected LocalDateTime lastUpdate;
	@JsonInclude
	private String isApplicableForNewsEvents;
}
