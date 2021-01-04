package com.enewschamp.domain.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusTransitionDTO {

	public static final String REVERSE_STATE = "Reverse";
	private String entityName;
	private String entityId;
	private String fromStatus;
	private String action;
	private String toStatus;

}
