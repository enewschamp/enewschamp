package com.enewschamp.app.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminSearchRequest {
	private String countryId;
	private String stateId;
	private String name;
	private String newsEventsApplicable;
	private String studentId;
	private String createDateFrom;
	private String createDateTo;
	private String categoryId;
	private String closeFlag;
	private String supportUserId;
}