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
@EqualsAndHashCode(callSuper=false)
@Table(name="StudentSubscription")
public class StudentSubscription extends BaseEntity{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "StudentID", length=10)
	private long studentID = 0L;
	
	@NotNull
	@Column(name = "EditionID", length=6)
	private String editionID ;
	
	@NotNull
	@Column(name = "SubscriptionType",length=1)
	private String subscriptionSelected;
	
	@NotNull
	@Column(name = "StartDate")
	private LocalDate startDate;
	
	@Column(name = "EndDate")
	private LocalDate endDate;
	
	@Column(name = "AutoRenewal",length=1)
	private String autoRenewal;
	
	
	
}
