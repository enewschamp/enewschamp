package com.enewschamp.subscription.app.dto;

import java.util.List;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentSubscriptionPeriodWorkDTO extends AbstractDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentID;
	private String emailId;
	private int subscriptionPeriodSelected;
	private Long feeAmount=0L;
	private String autoRenew;
	private String incompeleteFormText;
	private  List<String> feeText;
	private List<String> fee;
	
	

	
}
