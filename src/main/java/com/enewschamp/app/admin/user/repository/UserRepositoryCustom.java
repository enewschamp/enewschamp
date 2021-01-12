package com.enewschamp.app.admin.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.user.domain.entity.User;

public interface UserRepositoryCustom {
	public Page<User> findUsers(Pageable pageable, AdminSearchRequest searchRequest);
}
