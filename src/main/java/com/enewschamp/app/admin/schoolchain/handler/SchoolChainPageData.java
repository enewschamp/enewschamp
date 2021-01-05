package com.enewschamp.app.admin.schoolchain.handler;

import java.time.LocalDateTime;

import javax.persistence.Column;
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
	@Column(name = "name", length = 99)
	private String name;

	@NotNull
	@Column(name = "countryId")
	private String countryId;

	@NotNull
	@Column(name = "stateId")
	private String stateId;

	@NotNull
	@Column(name = "cityId")
	private String cityId;
	@NotNull
	@NotEmpty
	private String presence;
	@NotNull
	@NotEmpty
	private String eduBoard;
	@NotNull
	@NotEmpty
	private String eduMedium;
	@NotNull
	@NotEmpty
	private String genderDiversity;
	@NotNull
	@NotEmpty
	private String feeStructure;
	@NotNull
	@NotEmpty
	private String ownership;
	@NotNull
	@NotEmpty
	private String schoolProgram;
	@NotNull
	@NotEmpty
	private String shiftDetails;
	@NotNull
	@NotEmpty
	private String studentResidences;
	@NotNull
	@NotEmpty
	private String website;
	@NotNull
	@NotEmpty
	private String comments;
	@NotNull
	@NotEmpty
	private String operator;
	@NotNull
	@NotEmpty
	private LocalDateTime lastUpdate;

}
