package com.enewschamp.app.admin.user.role.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.user.domain.entity.UserRole;

public interface UserRoleRepositoryCustom {
	public Page<UserRole> findUserRoles(Pageable pageable, AdminSearchRequest searchRequest);
}
