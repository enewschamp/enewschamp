package com.enewschamp.app.admin.entitlement.repository;

import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

@JaversSpringDataAuditable
public interface EntitlementRepository extends JpaRepository<Entitlement, Long> {
	public Optional<Entitlement> findByUserIdAndRole(String userId, String role);
	public Optional<Entitlement> findByUserIdAndRoleAndPageNameAndAction(String userId, String role, String pageName, String action);

}
