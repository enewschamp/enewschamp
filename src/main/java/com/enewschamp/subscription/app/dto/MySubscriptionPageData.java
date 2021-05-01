package com.enewschamp.subscription.app.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionHistory;
import com.enewschamp.user.app.dto.StudentRefundDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MySubscriptionPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private StudentSubscriptionDTO subscription;

	@JsonInclude
	private List<StudentSubscriptionHistory> subscriptionHistory;

	@JsonInclude
	private List<StudentRefundDTO> refundHistory;

	@JsonInclude
	private List<StudentPaymentDTO> paymentHistory;

	@JsonInclude
	private List<StudentPaymentDTO> failedPaymentHistory;

}
