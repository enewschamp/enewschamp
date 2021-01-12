package com.enewschamp.app.admin.user.leave.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.user.domain.entity.UserLeave;

public interface UserLeaveRepositoryCustom {
	public Page<UserLeave> findUserLeaves(AdminSearchRequest searchRequest, Pageable pageable);
}