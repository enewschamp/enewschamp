package com.enewschamp.publication.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.app.admin.dashboard.handler.AvatarView;
import com.enewschamp.publication.domain.common.LOVProjection;
import com.enewschamp.publication.domain.entity.Avatar;

@JaversSpringDataAuditable
interface AvatarRepository extends JpaRepository<Avatar, Long> {

	@Query(value = "select a.avatarId as id, a.nameId as name " + "from Avatar a")
	public List<LOVProjection> getAvatarLOV();

	@Query(value = "select a from Avatar a")
	public List<Avatar> getAvatarList();

	public List<AvatarView> findAllProjectedBy();
}
