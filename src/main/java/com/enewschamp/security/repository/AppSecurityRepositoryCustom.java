package com.enewschamp.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.security.entity.AppSecurity;

public interface AppSecurityRepositoryCustom {
	public Page<AppSecurity> findAppSecurities(Pageable pageable);
}
