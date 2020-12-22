package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentSubscriptionPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private String emailId;

	@JsonInclude
	private String subscriptionSelected;

	@JsonInclude
	private String tncChecked;

	@JsonInclude
	private String validity;
}
