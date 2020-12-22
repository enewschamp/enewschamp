package com.enewschamp.app.student.notification;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "StudentUpdates")
public class StudentUpdates extends BaseEntity {

	private static final long serialVersionUID = 4683001717131495711L;

	@Id
	@NotNull
	@Column(name = "StudentId")
	private Long studentId;

	@NotNull
	@Column(name = "LastLoginDateTime")
	protected LocalDateTime lastLoginDateTime;

	@Column(name = "NotificationsViewDateTime")
	protected LocalDateTime notificationsViewDateTime;

	@Column(name = "NotificationsSinceLastView")
	protected Integer notificationsSinceLastView;

}
