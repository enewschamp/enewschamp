package com.enewschamp.app.school.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "SchoolPricing", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "editionId", "effectiveDate", "institutionId", "institutionType" }) })
@EqualsAndHashCode(callSuper = false)
public class SchoolPricing extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_pricing_id_generator")
	@SequenceGenerator(name = "school_pricing_id_generator", sequenceName = "school_pricing_id_seq", allocationSize = 1)
	@Column(name = "schoolPricingId", updatable = false, nullable = false)
	private Long schoolPricingId;

	@Column(name = "institutionId", nullable = false)
	private Long institutionId;

	@NotNull
	@Column(name = "institutionType", length = 1)
	private String institutionType;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;

	@NotNull
	@Column(name = "effectiveDate")
	private LocalDate effectiveDate;

	@Column(name = "pricingDetails", length = 2000)
	private String pricingDetails;
}
