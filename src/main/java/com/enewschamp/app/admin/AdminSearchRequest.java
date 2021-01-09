package com.enewschamp.app.admin;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminSearchRequest {
	private String countryId;
	private String cityId;
	private String stateId;
	private String name;
	private String surname;
	private String newsEventsApplicable;
	private Long studentId;
	private LocalDateTime createDateFrom;
	private LocalDateTime createDateTo;
	private String categoryId;
	private String closeFlag;
	private String supportUserId;
	private String schoolChainId;
	private String eduBoard;
	private String ownership;
	private String schoolProgram;
	private String schoolProgramCode;
	private String institutionId;
	private String institutionType;
	private String addressType;
	private String stakeholderId;
	private String schoolId;
	private String editionId;
}
