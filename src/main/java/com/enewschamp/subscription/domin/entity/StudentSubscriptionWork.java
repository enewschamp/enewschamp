package com.enewschamp.subscription.domin.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name="StudentSubscription_Work")
public class StudentSubscriptionWork extends BaseEntity{
	
	
	@NotNull
	@Column(name = "StudentID", length=10)
	private long studentID = 0L;
	
	@NotNull
	@Column(name = "EditionID", length=6)
	private long editionID = 0L;
	
	@NotNull
	@Column(name = "SubscriptionType",length=1)
	private String subscriptionType;
	
	@NotNull
	@Column(name = "StartDate")
	private Date startDate;
	
	@NotNull
	@Column(name = "EndDate")
	private Date endDate;
	
	@NotNull
	@Column(name = "AutoRenewal",length=1)
	private String autoRenewal;
	
	
	
}
