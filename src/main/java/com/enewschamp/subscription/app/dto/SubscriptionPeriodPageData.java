package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SubscriptionPeriodPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private Long studentId;
	@JsonInclude
	private String subscriptionPeriodSelected;
	@JsonInclude
	private Double subscriptionFee;
	@JsonInclude
	private String subscriptionFeeCurrency;
	@JsonInclude
	private String autoRenew;
	@JsonInclude
	private String pricingDetails;
}
