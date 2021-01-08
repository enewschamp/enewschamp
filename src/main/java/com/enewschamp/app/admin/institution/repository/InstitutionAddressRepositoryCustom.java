package com.enewschamp.app.admin.institution.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.institution.entity.InstitutionAddress;

public interface InstitutionAddressRepositoryCustom {
	public Page<InstitutionAddress> findInstitutionAddresses(Pageable pageable, AdminSearchRequest searchRequest);
}
