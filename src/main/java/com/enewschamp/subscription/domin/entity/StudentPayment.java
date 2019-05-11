package com.enewschamp.subscription.domin.entity;

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
@EqualsAndHashCode(callSuper=false)
@Table(name="StudentPayment")
public class StudentPayment extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentPay_id_generator")
	@SequenceGenerator(name="studentPay_id_generator", sequenceName = "studentPay_seq", allocationSize=1)
	private Long paymentID=0L;
	
	@NotNull
	@Column(name="StudentID", length=10)
	private Long studentID=0L;
	@NotNull
	@Column(name="EditionID", length=6)
	private String editionID;
	@NotNull
	@Column(name="SubscriptionType", length=1)
	private String subscriptionType;
	
	@Column(name="StartDate")
	private Date startDate;
	
	@Column(name="EndDate")
	private Date endDate;
	
	@Column(name="PayCurrency", length=4)
	private String payCurrency;
	
	@Column(name="PayCurrency", length=9)
	private Double payAmount;
	
	
}
