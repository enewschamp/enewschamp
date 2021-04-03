package com.enewschamp.app.admin.schoolchain.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.admin.schoolchain.entity.SchoolChain;

@JaversSpringDataAuditable
public interface SchoolChainRepository extends JpaRepository<SchoolChain, Long> {
}
