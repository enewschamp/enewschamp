package com.enewschamp.app.admin.properties.frontend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.common.domain.entity.PropertiesFrontend;

public interface PropertiesFrontendRepositoryCustom {
	public Page<PropertiesFrontend> findPropertiesFrontends(Pageable pageable, AdminSearchRequest searchRequest);
}
