package com.enewschamp.subscription.app.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentControlWorkDTO extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long studentId;
	private String emailId;
	private String studentDetails;
	private String studentDetailsW;
	private String schoolDetails;
	private String schoolDetailsW;
	private String subscriptionType;
	private String subscriptionTypeW;
	private String preferences;
	private String preferencesW;
	private String emailIdVerified;
	private String evalAvailed;
	private Long boUserComments;
	private Long boAuthComments;
	private String nextPageName;
	private String nextPageOperation;
	private String nextPageLoadMethod;
}
