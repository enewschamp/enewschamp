package com.enewschamp.app.admin.errorcode.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.common.domain.entity.ErrorCodes;

public interface ErrorCodesRepositoryCustom {
	public Page<ErrorCodes> findErrorCodes(Pageable pageable, AdminSearchRequest searchRequest);
}