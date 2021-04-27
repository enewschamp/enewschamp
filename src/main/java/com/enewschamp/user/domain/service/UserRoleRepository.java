package com.enewschamp.user.domain.service;

import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.user.domain.entity.UserRole;

@JaversSpringDataAuditable
interface UserRoleRepository extends JpaRepository<UserRole, Long> {

	@Query("Select c from UserRole c where c.userId= :userId and c.roleId =:roleId ")
	public Optional<UserRole> getByUserIdAndRole(@Param("userId") String userId, @Param("roleId") String roleId);

	@Query("Select c from UserRole c where c.userId= :userId and c.module= :module")
	public Optional<UserRole> getByUserId(@Param("userId") String userId, @Param("module") String module);

}