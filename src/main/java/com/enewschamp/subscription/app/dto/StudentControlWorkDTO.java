package com.enewschamp.subscription.app.dto;


import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentControlWorkDTO extends AbstractDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentID= 0L;
	
	private String eMail;
	
	private String studentDetails;
	
	private String studentPhoto;
	private String schoolDetails;
	
	private String subscriptionType;
	
	private String preferences;
	
	private String eMailVerified;
	
	private String evalAvailed;
	
	private Long boUserComments;
	
	private Long boAuthComments;

}
