package com.enewschamp.app.admin.institutionstakeholder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.institutionstakeholder.entity.InstitutionStakeholder;

public interface InstitutionStakeholderRepositoryCustom {
	public Page<InstitutionStakeholder> findInstitutionalStakeHolders(Pageable pageable,
			AdminSearchRequest searchRequest);
}
