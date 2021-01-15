package com.enewschamp.app.admin.uicontrols.global.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.common.uicontrols.entity.UIControlsGlobal;

public interface UIControlsGlobalRepositoryCustom {
	public Page<UIControlsGlobal> findUIControlsGlobals(Pageable pageable, AdminSearchRequest searchRequest);
}

