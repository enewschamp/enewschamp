package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentPreferencesWorkDTO extends AbstractDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentID;
	
	private String readingLevel;
	
	private String newsPDFoverEmail;
	
	private String scoresOverEmail;
	
	private String notificationsOverEmail;
	
	private String emailForComms;
	

}
