package com.enewschamp.subscription.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class StudentPreferenceCommWork {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "dailyPublication", length = 1)
	private String dailyPublication;

	@Column(name = "scoresProgressReports", length = 1)
	private String scoresProgressReports;

	@Column(name = "alertsNotifications", length = 1)
	private String alertsNotifications;

	@Column(name = "commsEmailId", length = 100)
	private String commsEmailId;

}
