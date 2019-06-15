package com.enewschamp.app.common.city.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;

@Data
public class CityDTO extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long cityId;
	
	private String nameId;

	private String description;
	
	
	private String stateId;
	
	private String countryId;
	

}
