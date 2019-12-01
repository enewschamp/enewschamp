package com.enewschamp.subscription.app.dto;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentSubscriptionPageData extends PageData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String terms;
	private String subscriptionSelected;
	private String emailID;
	private String incompeleteFormText;
	private String whatYouGetTextStandard;
	private String whatYouGetTextPremium;
	private String whatYouGetTextSchool;
	private String TermsOfUseText;
	private String PrivacyPolicy;
	
}
