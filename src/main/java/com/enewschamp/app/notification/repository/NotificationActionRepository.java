package com.enewschamp.app.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.notification.NotificationAction;

public interface NotificationActionRepository extends JpaRepository<NotificationAction, Long> {
}
