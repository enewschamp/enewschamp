package com.enewschamp.app.admin.page.navigator.rule.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.fw.page.navigation.entity.PageNavigatorRules;

public interface PageNavigatorRulesRepositoryCustom {
	public Page<PageNavigatorRules> findPageNavigatorRules(Pageable pageable, AdminSearchRequest searchRequest);
}