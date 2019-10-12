package com.enewschamp.app.common.city.dto;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CityDTO extends MaintenanceDTO { 

	private static final long serialVersionUID = -4856450567308583126L;

	private Long cityId;
	
	private String nameId;

	private String description;
	
	
	private String stateId;
	
	private String countryId;
	
	private boolean isApplicableForNewsEvents = false;

}
