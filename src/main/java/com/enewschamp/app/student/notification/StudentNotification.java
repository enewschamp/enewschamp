package com.enewschamp.app.student.notification;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "StudentNotifications")
public class StudentNotification extends BaseEntity {

	private static final long serialVersionUID = 4683001717131495711L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_notification_id_generator")
	@SequenceGenerator(name = "student_notification_id_generator", sequenceName = "student_notification_id_seq", allocationSize = 1)
	@Column(name = "StudentNotificationId", updatable = false, nullable = false)
	private Long studentNotificationId;

	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId;

	@NotNull
	@Column(name = "EditionId", length = ForeignKeyColumnLength.EditionId)
	private String editionId;

	@NotNull
	@Column(name = "NotificationId")
	private Long notificationId;

	@NotNull
	@Column(name = "NotificationDate")
	private LocalDateTime notificationDate;

	@Column(name = "ReadFlag", length = 1)
	@Enumerated(EnumType.STRING)
	private ReadFlag readFlag;

	@Column(name = "ReadDateTime")
	protected LocalDateTime readDateTime;

}
