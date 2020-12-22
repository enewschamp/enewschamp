package com.enewschamp.app.common.state.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.common.state.entity.State;

public interface StateRepositoryCustom {
	public Page<State> findStates(AdminSearchRequest searchRequest, Pageable pageable);
}
