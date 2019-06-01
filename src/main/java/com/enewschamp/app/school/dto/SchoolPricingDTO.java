package com.enewschamp.app.school.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SchoolPricingDTO extends AbstractDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	private Long institutionId;
	
	@NotNull
	private String institutionType;
	
	@NotNull
	private String editionId;
	
	@NotNull
	private LocalDate startDate;
	
	@NotNull
	private LocalDate endDate;
	
	
	private String feeCurrency;
	
	private String feeMonthly;
	
	private String feeQuarterly;
	
	private String feeHalfYearly;
	
	private String feeYearly;
		
}
