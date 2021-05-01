package com.enewschamp.app.notification.page.data;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NotificationsSearchRequest {
	private String editionId;
	private String isRead;
	private Long studentId;
	private LocalDateTime operationDateTime;
	private String countOnly;
}
