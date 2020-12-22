package com.enewschamp.subscription.domain.entity;

import java.util.Date;

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
@Table(name = "StudentPaymentWork")
public class StudentPaymentWork extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentPayment_id_generator")
	@SequenceGenerator(name = "studentPayment_id_generator", sequenceName = "studentPayment_seq", allocationSize = 1)
	private Long paymentId = 0L;

	@NotNull
	@Column(name = "StudentId", length = 10)
	private Long studentId = 0L;
	@NotNull
	@Column(name = "EditionId", length = 6)
	private String editionId;
	@NotNull
	@Column(name = "SubscriptionType", length = 1)
	private String subscriptionType;

	@NotNull
	@Column(name = "SubscriptionPeriod", length = 100)
	private String subscriptionPeriod;

	@Column(name = "PayCurrency", length = 4)
	private String payCurrency;

	@Column(name = "PayAmount", length = 9)
	private Double payAmount;

	@Column(name = "OrderId")
	private String orderId;

}
