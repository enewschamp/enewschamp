package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentPreferencePageData  extends PageData {

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
