package com.enewschamp.user.domin.service;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.user.domin.entity.UserRole;
import com.enewschamp.user.domin.entity.UserRoleKey;

@JaversSpringDataAuditable
interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey>{ 
	 
} 