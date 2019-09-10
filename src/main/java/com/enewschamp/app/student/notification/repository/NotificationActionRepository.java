package com.enewschamp.app.student.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.student.notification.NotificationAction;

public interface NotificationActionRepository extends JpaRepository<NotificationAction, Long>{

	
}
