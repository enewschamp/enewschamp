package com.enewschamp.subscription.app.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentSubscriptionDTO extends AbstractDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(max=10)
	private long studentID = 0L;
	
	@NotNull
	@Size(max=6)
	private String editionID ;
	
	@NotNull
	@Size(max=1)
	private String subscriptionSelected;
	
	@NotNull
	private Date startDate;
	
	@NotNull
	private Date endDate;
	
	@NotNull
	@Size(max=1)
	private String autoRenewal;
	
	

	
}
