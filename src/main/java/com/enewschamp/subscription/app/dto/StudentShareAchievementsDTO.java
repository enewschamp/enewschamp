package com.enewschamp.subscription.app.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentShareAchievementsDTO extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentId;

	private String personalisedMessage;

	private String approvalRequired;

	private String recipientName1;

	private String recipientName2;

	private String recipientName3;

	private String recipientName4;

	private String recipientName5;

	private String recipientName6;

	private String recipientName7;

	private String recipientName8;

	private String recipientName9;

	private String recipientName10;

	private String recipientGreeting1;

	private String recipientGreeting2;

	private String recipientGreeting3;

	private String recipientGreeting4;

	private String recipientGreeting5;

	private String recipientGreeting6;

	private String recipientGreeting7;

	private String recipientGreeting8;

	private String recipientGreeting9;

	private String recipientGreeting10;

	private String recipientContact1;

	private String recipientContact2;

	private String recipientContact3;

	private String recipientContact4;

	private String recipientContact5;

	private String recipientContact6;

	private String recipientContact7;

	private String recipientContact8;

	private String recipientContact9;

	private String recipientContact10;

}