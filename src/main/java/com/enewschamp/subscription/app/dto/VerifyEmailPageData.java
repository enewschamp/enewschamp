package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VerifyEmailPageData  extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentID;
	
	private String readingLevel;
	
	private StudentPreferenceCommPageData communications;

}
