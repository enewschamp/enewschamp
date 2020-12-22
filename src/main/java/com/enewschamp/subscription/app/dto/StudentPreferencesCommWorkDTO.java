package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentPreferencesCommWorkDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String dailyPublication;

	private String scoresProgressReports;

	private String alertsNotifications;

	private String commsEmailId;

}
