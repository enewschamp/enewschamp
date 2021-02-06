package com.enewschamp.app.admin.student.payment.handler;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentPaymentPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long paymentId = 0L;
	private Long studentId = 0L;
	private String editionId;
	private String subscriptionType;
	private String subscriptionPeriod;
	private String paymentCurrency;
	private Double paymentAmount;

}
