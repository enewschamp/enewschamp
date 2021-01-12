package com.enewschamp.app.admin.entitlement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;

public interface EntitlementRepositoryCustom {
	public Page<Entitlement> findEntitlements(Pageable pageable, AdminSearchRequest searchRequest);
}
