package com.enewschamp.app.admin.stakeholder.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.admin.stakeholder.entity.StakeHolder;

@JaversSpringDataAuditable
public interface StakeHolderRepository extends JpaRepository<StakeHolder, Long> {

}
