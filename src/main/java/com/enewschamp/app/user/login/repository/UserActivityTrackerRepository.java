package com.enewschamp.app.user.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.user.login.entity.UserActivityTracker;

public interface UserActivityTrackerRepository extends JpaRepository<UserActivityTracker, Long> {

}
