package com.enewschamp.app.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.helpdesk.entity.HelpDesk;

public interface HelpDeskRepositoryCustom {
	public Page<HelpDesk> findHelpDesks(Pageable pageable);
}
