package com.enewschamp.app.admin.stakeholder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.stakeholder.entity.StakeHolder;

public interface StakeHolderRepositoryCustom {
	public Page<StakeHolder> findStakeHolders(Pageable pageable, AdminSearchRequest searchRequest);
}
