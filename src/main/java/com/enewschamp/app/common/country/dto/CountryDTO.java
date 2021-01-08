package com.enewschamp.app.common.country.dto;

import com.enewschamp.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CountryDTO extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long countryId;

	@JsonProperty(value = "id")
	private String nameId;

	@JsonProperty(value = "name")
	private String description;

	private int isd;

	private String currencyId;

	private String languageId;

}
