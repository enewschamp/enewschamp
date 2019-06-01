package com.enewschamp.subscription.pricing.dto;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class IndividualPricingDTO extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long individualPricingId;

	@NotNull
	private String editionId;
	
	@NotNull
	private LocalDate effectiveDate;
	
	private String feeCurrency;
	
	private String feeMonthly;
	
	private String feeQuarterly;
	
	private String feeHalfYearly;
	
	private String feeYearly;
	
}
