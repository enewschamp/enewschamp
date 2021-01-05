package com.enewschamp.app.helpdesk.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.helpdesk.entity.HelpDesk;

@JaversSpringDataAuditable
public interface HelpDeskRepository extends JpaRepository<HelpDesk, Long> {

}
