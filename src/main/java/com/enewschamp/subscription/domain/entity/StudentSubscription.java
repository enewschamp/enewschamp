package com.enewschamp.subscription.domain.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentSubscription")
public class StudentSubscription extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;

	@NotNull
	@Column(name = "subscriptionType", length = 1)
	private String subscriptionSelected;

	@Column(name = "subscriptionPeriod")
	private String subscriptionPeriod;

	@NotNull
	@Column(name = "startDate")
	private LocalDate startDate;

	@Column(name = "endDate")
	private LocalDate endDate;

	@Column(name = "autoRenewal", length = 1)
	private String autoRenewal;

	@Column(name = "subscriptionId", length = 1)
	private String subscriptionId;

	@Column(name = "subscriptionAmountType", length = 10)
	private String subscriptionAmountType;

	@Column(name = "subscriptionFrequency", length = 10)
	private String subscriptionFrequency;

	@Column(name = "subscriptionFrequencyUnit", length = 10)
	private String subscriptionFrequencyUnit;

	@Column(name = "subscriptionExpiryDate", length = 10)
	private String subscriptionExpiryDate;
}
