package com.enewschamp.app.student.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.student.notification.StudentNotification;

public interface StudentNotificationRepository extends JpaRepository<StudentNotification, Long>{

	
}
