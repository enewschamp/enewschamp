package com.enewschamp.subscription.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Embeddable
public class StudentPreferenceComm {
	/**
	 */
	
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value="dailyPublication")
	@Column(name="NewsPDFoverEmail", length=1)
	private String newsPDFoverEmail;
	
	@JsonProperty(value="scoresProgressReports")
	@Column(name="ScoresOverEmail", length=1)
	private String scoresOverEmail;
	
	@JsonProperty(value="alertsNotifications")
	@Column(name="NotificationsOverEmail", length=1)
	private String notificationsOverEmail;
	
	@JsonProperty(value="over")
	@Column(name="EmailForComms", length=1)
	private String emailForComms;
	
}
