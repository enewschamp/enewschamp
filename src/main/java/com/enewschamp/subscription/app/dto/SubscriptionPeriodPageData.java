package com.enewschamp.subscription.app.dto;

import java.util.List;
import java.util.Map;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SubscriptionPeriodPageData  extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentID;
	
	private int subscriptionPeriodSelected;
	private Double feeAmount;
	private String autoRenew;
	private String incompeleteFormText;
	//private  List<String> feeText;
	private Map<String, String > feeText;
	private List<String> fee;
	private Double payAmount;
	private String payCurrency;

	@JsonIgnore
	private String feeMonthly;
	@JsonIgnore
	private String feeQuarterly;
	@JsonIgnore
	private String feeHalfYearly;
	@JsonIgnore
	private String feeYearly;
	@JsonIgnore
	private String feeCurrency;
	
}
