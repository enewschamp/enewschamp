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
	private Long studentId;
	private String email;
	private String studentDetails;
	private String studentDetailsW;
	private String studentPhoto;
	private String studentPhotoW;
	private String schoolDetails;
	private String schoolDetailsW;
	private String subscriptionType;
	private String subscriptionTypeW;
	private String preferences;
	private String preferencesW;
	private String emailVerified;
	private String evalAvailed;
	private Long boUserComments;
	private Long boAuthComments;
	private String nextPageName;
	private String nextPageOperation;
	private String nextPageLoadMethod;
}
