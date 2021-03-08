package com.enewschamp.subscription.domain.entity;

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
@Table(name = "StudentSubscriptionWork")
public class StudentSubscriptionWork extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId;

	@NotNull
	@Column(name = "EditionId", length = 6)
	private String editionId;

	@NotNull
	@Column(name = "SubscriptionType", length = 1)
	private String subscriptionSelected;

	@Column(name = "SubscriptionPeriod")
	private String subscriptionPeriod;

	@Column(name = "AutoRenewal", length = 1)
	private String autoRenewal;

}
