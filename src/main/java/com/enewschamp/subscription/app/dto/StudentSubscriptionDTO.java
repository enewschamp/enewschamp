package com.enewschamp.subscription.app.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;

@Data
public class StudentSubscriptionDTO extends AbstractDTO{

	@NotNull
	@Size(max=10)
	private long studentID = 0L;
	
	@NotNull
	@Size(max=6)
	private long editionID = 0L;
	
	@NotNull
	@Size(max=1)
	private String subscriptionType;
	
	@NotNull
	private LocalDate startDate;
	
	@NotNull
	private LocalDate endDate;
	
	@NotNull
	@Size(max=1)
	private String autoRenewal;
	
	

	
}
