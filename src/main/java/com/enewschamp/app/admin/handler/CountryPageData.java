package com.enewschamp.app.admin.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CountryPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	@NotNull(message = "Country id must not be null")
	private Long countryId;
	@JsonInclude
	@NotNull(message = "ISD number must not be null")
	private int isd;
	@NotNull
	@JsonInclude
	@NotNull(message = "Name must not be null")
	private String name;
	@JsonInclude
	@NotNull(message = "Description must not be null")
	private String description;
	@JsonInclude
	private String currencyId;
	@JsonInclude
	private String language;
	@JsonInclude
	private String recordInUse;
	@JsonInclude
	private String operator;
	@JsonInclude
	protected LocalDateTime lastUpdate;
}
