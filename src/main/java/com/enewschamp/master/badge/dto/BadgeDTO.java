package com.enewschamp.master.badge.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BadgeDTO extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String badgeId;
	
	private String nameId;
	
	private String name;

	private String genreId;
	
	private Long monthlyPointsToScore;
	
	private String image;
	
	private String editionId;
	
}
