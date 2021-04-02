package com.enewschamp.subscription.app.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentSubscriptionDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(max = 10)
	private Long studentId = 0L;

	@NotNull
	@Size(max = 6)
	private String editionId;

	@NotNull
	@Size(max = 1)
	private String subscriptionSelected;

	private LocalDate startDate;

	private LocalDate endDate;

	private String autoRenewal;

	private String subscriptionId;

	private String subscriptionAmountType;

	private String subscriptionFrequency;

	private String subscriptionFrequencyUnit;

	private String subscriptionExpiryDate;

}
