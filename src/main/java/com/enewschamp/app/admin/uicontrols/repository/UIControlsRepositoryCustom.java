package com.enewschamp.app.admin.uicontrols.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.common.uicontrols.entity.UIControls;

public interface UIControlsRepositoryCustom {
	public Page<UIControls> findUIControls(Pageable pageable, AdminSearchRequest searchRequest);
}
