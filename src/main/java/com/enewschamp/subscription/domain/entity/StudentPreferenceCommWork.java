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
	
	@Column(name="NewsPDFoverEmail", length=1)
	private String newsPDFoverEmail;
	
	@Column(name="ScoresOverEmail", length=1)
	private String scoresOverEmail;
	
	@Column(name="NotificationsOverEmail", length=1)
	private String notificationsOverEmail;
	
	@Column(name="EmailForComms", length=1)
	private String emailForComms;
	
}
