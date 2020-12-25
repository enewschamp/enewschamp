package com.enewschamp.app.admin.entitlement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntitlementRepositoryCustom {
	public Page<Entitlement> findEntitlements(Pageable pageable);
}
