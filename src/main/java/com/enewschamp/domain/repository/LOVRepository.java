package com.enewschamp.domain.repository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.domain.entity.LOV;
import com.enewschamp.publication.domain.common.LOVProjection;

@JaversSpringDataAuditable
public interface LOVRepository extends JpaRepository<LOV, Long> {

	/*
	 * @Query(value =
	 * "select a.lovId as id, b.text as name, a.description as description from LOV a, MultiLanguageText b where a.nameId=b.multiLanguageTextId"
	 * ) public List<LOVProjection> getLOV(String lovType);
	 */

	@Query(value = "select a.nameId as id, a.description as name from LOV a where a.type=:lovType")
	public List<LOVProjection> getLOV(@Param("lovType") String lovType);
}