package com.enewschamp.app.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.helpdesk.entity.HelpDesk;

public interface HelpDeskRepository extends JpaRepository<HelpDesk, Long>{

	
}
