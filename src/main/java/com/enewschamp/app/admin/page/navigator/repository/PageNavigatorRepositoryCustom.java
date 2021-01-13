package com.enewschamp.app.admin.page.navigator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.fw.page.navigation.entity.PageNavigator;

public interface PageNavigatorRepositoryCustom {
	public Page<PageNavigator> findPageNavigators(Pageable pageable, AdminSearchRequest searchRequest);
}
