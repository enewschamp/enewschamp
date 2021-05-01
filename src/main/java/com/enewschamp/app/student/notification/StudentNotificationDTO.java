package com.enewschamp.app.student.notification;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.enewschamp.app.common.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Entity
public class StudentNotificationDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	@Id
	@JsonInclude
	private Long studentNotificationId;

	@JsonInclude
	private Long studentId;

	@JsonInclude
	private String editionId;

	@JsonInclude
	private Long notificationId;

	@JsonInclude
	protected String notificationText;

	@JsonInclude
	protected LocalDateTime notificationDate;

	@JsonInclude
	private ReadFlag readFlag;

	@JsonInclude
	protected LocalDateTime readDateTime;

}