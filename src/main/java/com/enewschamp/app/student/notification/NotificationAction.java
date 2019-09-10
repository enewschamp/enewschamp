package com.enewschamp.app.student.notification;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="NotificationActions")
public class NotificationAction extends BaseEntity {	
	
	private static final long serialVersionUID = 8250276383332940667L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notify_action_id_generator")
	@SequenceGenerator(name="notify_action_id_generator", sequenceName = "notify_action_id_seq", allocationSize=1)
	@Column(name = "NotificationActionId", updatable = false, nullable = false)
	private Long notificationActionId;

	@Column(name = "StudentId")
	private Long studentId;
	
	@Column(name = "NotificationActionType")
	@Enumerated(EnumType.STRING)
	private NotificationActionType notificationActionType;
	
	@Column(name = "NotificationDate")
	private LocalDate notificationDate;
	
}
