package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentPreferencesCommWorkDTO extends AbstractDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty(value="dailyPublication")
	private String newsPDFoverEmail;
	
	@JsonProperty(value="scoresProgressReports")
	private String scoresOverEmail;
	
	@JsonProperty(value="alertsNotifications")
	private String notificationsOverEmail;
	
	@JsonProperty(value="over")
	private String emailForComms;
	

}
