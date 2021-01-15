package com.enewschamp.app.admin.properties.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.common.domain.entity.PropertiesBackend;

public interface PropertiesBackendRepositoryCustom {
	public Page<PropertiesBackend> findPropertiesBackends(Pageable pageable, AdminSearchRequest searchRequest);
}