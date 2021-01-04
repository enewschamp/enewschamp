package com.enewschamp.publication.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.publication.domain.entity.HashTag;

@JaversSpringDataAuditable
interface HashTagRepository extends JpaRepository<HashTag, String> {
	@Query("Select h from HashTag h where h.hashTag like (%:hashTag%) and h.recordInUse ='Y'")
	public List<HashTag> getHashTagByName(@Param("hashTag") String hashTag);
}