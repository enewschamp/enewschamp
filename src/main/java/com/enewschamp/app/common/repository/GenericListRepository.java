package com.enewschamp.app.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;

public interface GenericListRepository<T> {
	public Page<T> findAll(Pageable pageable, AdminSearchRequest searchRequest);
}
