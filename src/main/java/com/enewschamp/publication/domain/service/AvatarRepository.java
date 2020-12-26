package com.enewschamp.publication.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.publication.domain.common.LOVProjection;
import com.enewschamp.publication.domain.entity.Avatar;

@JaversSpringDataAuditable
interface AvatarRepository extends JpaRepository<Avatar, Long> {

	@Query(value = "select a.avatarId as id, b.text as name " + "from Avatar a, MultiLanguageText b "
			+ "where a.nameId=b.multiLanguageTextId")
	public List<LOVProjection> getAvatarLOV();

	@Query(value = "select a from Avatar a")
	public List<Avatar> getAvatarList();

}