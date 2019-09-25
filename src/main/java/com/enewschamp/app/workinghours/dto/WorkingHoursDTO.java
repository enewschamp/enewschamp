package com.enewschamp.app.workinghours.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class WorkingHoursDTO extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long workingHoursId;
	private String editionId;
	private Long timeZone;
	private Long startTime;
	private Long endTime;
	
}
