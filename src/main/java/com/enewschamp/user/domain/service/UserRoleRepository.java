package com.enewschamp.user.domain.service;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.user.domain.entity.UserRole;
import com.enewschamp.user.domain.entity.UserRoleKey;

@JaversSpringDataAuditable
interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey>{ 
	 
} 