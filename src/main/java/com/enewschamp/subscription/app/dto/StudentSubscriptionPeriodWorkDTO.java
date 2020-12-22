package com.enewschamp.subscription.app.dto;

import java.util.List;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentSubscriptionPeriodWorkDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentId;
	private String emailId;
	private String subscriptionPeriodSelected;
	private Long feeAmount = 0L;
	private String autoRenew;
	private String incompleteFormText;
	private List<String> feeText;
	private List<String> fee;
}
