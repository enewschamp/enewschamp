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

	@NotNull
	private String emailId;
	
	@NotNull
	private String terms;
	
	@NotNull
	private String subscriptionSelected;
}
