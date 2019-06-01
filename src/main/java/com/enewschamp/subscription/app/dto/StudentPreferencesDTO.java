package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentPreferencesDTO extends AbstractDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentID;
	
	private String readingLevel;
	
	private StudentPreferencesCommDTO communications;

}
