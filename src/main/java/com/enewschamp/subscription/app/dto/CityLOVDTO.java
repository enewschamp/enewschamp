package com.enewschamp.subscription.app.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CityLOVDTO extends PageData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String stateID;
	private List<String> cityLOV;
	
}
