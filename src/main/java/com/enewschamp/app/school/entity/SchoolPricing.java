package com.enewschamp.app.school.entity;

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
@Table(name="SchoolPricing")
@EqualsAndHashCode(callSuper=false)
public class SchoolPricing extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schoolPricing_id_generator")
	@SequenceGenerator(name="schoolPricing_id_generator", sequenceName = "schoolPricing_seq", allocationSize=1)
	@Column(name = "schoolPricingId", updatable = false, nullable = false)
	private Long schoolPricingId;
	
	
	@Column(name = "institutionId", nullable = false)
	private Long institutionId;
	
	@NotNull
	@Column(name = "institutionType", length=1)
	private String institutionType;
	
	@NotNull
	@Column(name = "editionId", length=6)
	private String editionId;
	
	@NotNull
	@Column(name = "startDate")
	private LocalDate startDate;
	
	@NotNull
	@Column(name = "endDate")
	private LocalDate endDate;
	
	
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
