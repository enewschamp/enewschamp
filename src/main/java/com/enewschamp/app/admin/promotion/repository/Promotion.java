package com.enewschamp.app.admin.promotion.repository;

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
@Table(name = "Promotions",uniqueConstraints = {
		@UniqueConstraint(columnNames = { "editionId", "dateFrom", "countryId", "stateId", "cityId" }) })
@EqualsAndHashCode(callSuper = false)
public class Promotion extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promotion_id_generator")
	@SequenceGenerator(name = "promotion_id_generator", sequenceName = "promotion_id_seq", allocationSize = 1)
	@Column(name = "promotionId", updatable = false, nullable = false)
	private Long promotionId;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;

	@NotNull
	@Column(name = "couponCode", length = 10)
	private String couponCode;

	@NotNull
	@Column(name = "dateFrom")
	private LocalDate dateFrom;

	@NotNull
	@Column(name = "dateTo")
	private LocalDate dateTo;

	@NotNull
	@Column(name = "countryId", length = 2)
	private String countryId;

	@NotNull
	@Column(name = "stateId", length = 50)
	private String stateId;

	@NotNull
	@Column(name = "cityId", length = 50)
	private String cityId;

	@NotNull
	@Column(name = "promotionDetails", length = 2000)
	private String promotionDetails;

	@NotNull
	@Column(name = "description", length = 200)
	private String description;
}
