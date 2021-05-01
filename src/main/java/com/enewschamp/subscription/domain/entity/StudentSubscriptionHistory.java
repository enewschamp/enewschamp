package com.enewschamp.subscription.domain.entity;

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
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentSubscriptionHistory")
public class StudentSubscriptionHistory extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_subscription_id_generator")
	@SequenceGenerator(name = "student_subscription_id_generator", sequenceName = "student_subscription_id_seq", allocationSize = 1)
	private Long recordId = 0L;

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

	@Column(name = "orderId")
	private String orderId;

	@Column(name = "subscriptionId", length = 100)
	private String subscriptionId;

	@Column(name = "subscriptionAmountType", length = 10)
	private String subscriptionAmountType;

	@Column(name = "subscriptionFrequency", length = 10)
	private String subscriptionFrequency;

	@Column(name = "subscriptionFrequencyUnit", length = 10)
	private String subscriptionFrequencyUnit;

	@Column(name = "subscriptionExpiryDate", length = 10)
	private String subscriptionExpiryDate;

	@Column(name = "subscriptionEnableRetry", length = 1)
	private String subscriptionEnableRetry;
}
