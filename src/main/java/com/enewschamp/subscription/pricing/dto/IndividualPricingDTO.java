package com.enewschamp.subscription.pricing.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class IndividualPricingDTO extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long individualPricingId;

	@NotNull
	private String editionId;

	@NotNull
	private LocalDate effectiveDate;

	private String pricingDetails;

}
