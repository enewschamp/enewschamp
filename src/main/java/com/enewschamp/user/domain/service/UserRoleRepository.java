package com.enewschamp.user.domain.service;

import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.user.domain.entity.UserRole;
import com.enewschamp.user.domain.entity.UserRoleKey;

@JaversSpringDataAuditable
interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey>{ 
	
	
	@Query("Select c from UserRole c where c.userRoleKey.userId= :userId and c.userRoleKey.roleId =:roleId ")
	public Optional<UserRole> getByUserIdAndRole(@Param("userId") String userId, @Param("roleId") String roleId);
	
	
	 
} 