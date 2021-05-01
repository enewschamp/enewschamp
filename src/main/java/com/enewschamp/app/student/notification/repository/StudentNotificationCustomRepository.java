package com.enewschamp.app.student.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.notification.page.data.NotificationsSearchRequest;
import com.enewschamp.app.student.notification.StudentNotificationDTO;

public interface StudentNotificationCustomRepository {

	public Page<StudentNotificationDTO> getNotificationList(NotificationsSearchRequest searchRequest,
			Pageable pageable);
}
