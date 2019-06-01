package com.enewschamp.app.school.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SchoolDTO extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Long schoolId;
	
	
	private Long schoolChainId;
	
	private String schoolProgram;
	
	private String stateId;
	
	private String countryId;
	
	private String cityId;
	
	private String name;
	
	private String website;
	
	private String ownership ;
	
	private String eduBoard ;
	
	private String eduMedium ;
	

	private String genderDiversity ;
	
	
	private String studentResidences ;
	
	
	private String shiftDetails ;
	
	
	private String feeStructure ;
	
	
	private String comments ;
	
	
	
	
}
