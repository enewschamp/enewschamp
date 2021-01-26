package com.enewschamp.app.admin.entitlement.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

@JaversSpringDataAuditable
public interface EntitlementRepository extends JpaRepository<Entitlement, Long> {

}
