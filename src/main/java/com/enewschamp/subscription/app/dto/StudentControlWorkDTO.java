package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentControlWorkDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long studentID = 0L;
	private String operation;
	private String emailID;
	private String studentDetails;
	private String studentDetailsW;
	private String studentPhoto;
	private String studentPhotoW;
	private String schoolDetails;
	private String schoolDetailsW;
	private String subscriptionType;
	private String subscriptionTypeW;
	private String preferences;
	private String emailVerified;
	private String evalAvailed;
	private Long boUserComments;
	private Long boAuthComments;

}
