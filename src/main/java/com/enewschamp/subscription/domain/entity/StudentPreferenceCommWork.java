package com.enewschamp.subscription.domain.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import com.enewschamp.app.common.StringCryptoConverter;

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

	@Convert(converter = StringCryptoConverter.class)
	@Column(name = "commsEmailId", length = 100)
	private String commsEmailId;

}