package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentPreferencesCommPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private String dailyPublication;

	@JsonInclude
	private String scoresProgressReports;

	@JsonInclude
	private String alertsNotifications;

	@JsonInclude
	private String commsEmailId;

}
