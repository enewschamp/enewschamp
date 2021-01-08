package com.enewschamp.app.admin.schoolchain.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SchoolChainPageData extends PageData {
	
	private static final long serialVersionUID = 1L;
	
	private Long schoolChainId;

	@NotNull
	@NotEmpty
	private String name;

	@NotNull
	@NotEmpty
	private String countryId;
	
	@NotNull
	@NotEmpty
	private String presence;
	
	
	private String stateId;
	private String cityId;
	private String eduBoard;
	private String eduMedium;
	private String genderDiversity;
	private String feeStructure;
	private String ownership;
	private String schoolProgram;
	private String shiftDetails;
	private String studentResidences;
	private String website;
	private String comments;
	private String operator;
	private LocalDateTime lastUpdate;

}
