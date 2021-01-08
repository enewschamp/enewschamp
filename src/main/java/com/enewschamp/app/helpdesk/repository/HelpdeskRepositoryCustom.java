package com.enewschamp.app.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.helpdesk.entity.Helpdesk;

public interface HelpdeskRepositoryCustom {
	public Page<Helpdesk> findHelpDesks(Pageable pageable, AdminSearchRequest searchRequest);
}
