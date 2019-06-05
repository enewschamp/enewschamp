package com.enewschamp.domain.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusTransitionDTO {

	private String entityName;
	private String fromStatus;
	private String action;
	private String toStatus;
	
	
	
}
