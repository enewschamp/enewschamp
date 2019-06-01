package com.enewschamp.app.common.state.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StateDTO extends BaseEntity{

	private static final long serialVersionUID = 1L;

	private Long stateId;
	
	private String countryId;
	
	private String nameId;
	
	private String description;
	
}
