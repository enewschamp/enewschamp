package com.enewschamp.user.domin.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.publication.domain.common.LOVProjection;
import com.enewschamp.user.domin.entity.Role;

@JaversSpringDataAuditable
interface RoleRepository extends JpaRepository<Role, String>{ 
	 
	@Query(value="select a.roleId as id, a.roleName as name from Role a")
    public List<LOVProjection> getRoleLOV();
} 