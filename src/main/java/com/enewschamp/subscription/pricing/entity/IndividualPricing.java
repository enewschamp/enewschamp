package com.enewschamp.subscription.pricing.entity;

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
@Entity
@Table(name="IndividualPricing")
@EqualsAndHashCode(callSuper=false)
public class IndividualPricing extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "individualPricing_id_generator")
	@SequenceGenerator(name="individualPricing_id_generator", sequenceName = "individualPricing_seq", allocationSize=1)
	@Column(name = "individualPricingId", updatable = false, nullable = false)
	private Long individualPricingId;

	@NotNull
	@Column(name = "editionId", length=6)
	private String editionId;
	
	@NotNull
	@Column(name = "effectiveDate")
	private LocalDate effectiveDate;
	
	@Column(name = "feeCurrency", length=3)
	private String feeCurrency;
	
	@Column(name = "feeMonthly", length=8)
	private String feeMonthly;
	
	@Column(name = "feeQuarterly", length=8)
	private String feeQuarterly;
	
	@Column(name = "feeHalfYearly", length=8)
	private String feeHalfYearly;
	
	@Column(name = "feeYearly", length=8)
	private String feeYearly;
	
}
