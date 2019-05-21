package com.enewschamp.subscription.app.dto;

import java.util.Date;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentPaymentDTO extends AbstractDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long paymentID=0L;
	
	private Long studentID=0L;
	private String editionID;
	private String subscriptionType;
	
	private Date startDate;
	
	private Date endDate;
	
	private String payCurrency;
	
	private Double payAmount;

}
