package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentShareAchievementsDTO extends AbstractDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long StudentShareAchievementsId= 0L;
	
	private Long studentId;
	
	private String personalMessage;
	
	
	private String contact1;
	
	private String contact2;
	
	private String contact3;
	
	private String contact4;
	
	private String contact5;
	
	private String contact6;
	
	private String contact7;
	
	private String contact8;
	
	private String contact9;
	
	private String contact10;
	
}
