package com.enewschamp.subscription.app.dto;

import java.util.Date;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentPaymentDTO extends AbstractDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long paymentId = 0L;
	private Long studentId = 0L;
	private String editionId;
	private String subscriptionType;
	private String subscriptionPeriod;
	private String paymentCurrency;
	private Double paymentAmount;
}
