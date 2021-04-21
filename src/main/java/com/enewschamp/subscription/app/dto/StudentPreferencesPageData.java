package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentPreferencesPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private Long studentId;

	@JsonInclude
	private String readingLevel;

	@JsonInclude
	private String featureProfileInChamps;

	@JsonInclude
	private ChampPermissionsPageData champPermissions;

	@JsonInclude
	private StudentPreferencesCommPageData commsOverEmail;
}