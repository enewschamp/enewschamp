package com.enewschamp.domain.common;

import lombok.Data;

@Data
public class StatusTransitionDTO {

	private String entityName;
	private String fromStatus;
	private String action;
	private String toStatus;
	
}
