package com.enewschamp.app.admin.uicontrols.rule.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.common.uicontrols.entity.UIControlsRules;

public interface UIControlsRulesRepositoryCustom {
	public Page<UIControlsRules> findUIControlsRules(Pageable pageable, AdminSearchRequest searchRequest);
}
