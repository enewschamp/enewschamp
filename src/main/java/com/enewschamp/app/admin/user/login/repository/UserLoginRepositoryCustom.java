package com.enewschamp.app.admin.user.login.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.user.login.entity.UserLogin;

public interface UserLoginRepositoryCustom {
	public Page<UserLogin> findUserLogins(Pageable pageable, AdminSearchRequest searchRequest);
}
