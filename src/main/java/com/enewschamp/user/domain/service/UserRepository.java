package com.enewschamp.user.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.publication.domain.common.LOVProjection;
import com.enewschamp.user.domain.entity.User;

@JaversSpringDataAuditable
interface UserRepository extends JpaRepository<User, String> {

	@Query(value = "select a.userId as id, concat(a.name, ' ', a.surname) as name,a.isActive as isActive from User a, UserRole b"
			+ " where a.userId = b.userRoleKey.userId and b.userRoleKey.roleId = 'PUBLISHER'")
	public List<LOVProjection> getPublisherLOV();

	@Query(value = "select a.userId as id, concat(a.name, ' ', a.surname) as name,a.isActive as isActive from User a, UserRole b"
			+ " where a.userId = b.userRoleKey.userId and b.userRoleKey.roleId = 'AUTHOR'")
	public List<LOVProjection> getAuthorLOV();

	@Query(value = "select a.userId as id, concat(a.name, ' ', a.surname) as name,a.isActive as isActive from User a, UserRole b"
			+ " where a.userId = b.userRoleKey.userId and b.userRoleKey.roleId = 'EDITOR'")
	public List<LOVProjection> getEditorLOV();
}