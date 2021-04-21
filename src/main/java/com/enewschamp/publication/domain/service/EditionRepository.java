package com.enewschamp.publication.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.app.admin.dashboard.handler.EditionView;
import com.enewschamp.publication.domain.common.LOVProjection;
import com.enewschamp.publication.domain.entity.Edition;

@JaversSpringDataAuditable
interface EditionRepository extends JpaRepository<Edition, String> {

	@Query(value = "select a.editionId as id, a.editionName as name from Edition a")
	public List<LOVProjection> getEditionLOV();

	public List<EditionView> findAllProjectedBy();
}