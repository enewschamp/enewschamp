package com.enewschamp.app.admin.schoolchain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.enewschamp.app.admin.schoolchain.entity.SchoolChain;

@Repository
public interface SchoolChainRepositoryCustom {
	public Page<SchoolChain> findSchoolChains(Pageable pageable);
}
