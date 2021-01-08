package com.enewschamp.common.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.common.domain.entity.PropertiesBackend;
import com.enewschamp.publication.domain.common.LOVProjection;

@JaversSpringDataAuditable
interface PropertiesBackendRepository extends JpaRepository<PropertiesBackend, Long> {

	@Query(value = "select a.name as id, a.value as name from PropertiesBackend a")
	public List<LOVProjection> getPropertiesLOV();

	@Query(value = "select a from PropertiesBackend a where a.appName like concat('%',:appName,'%')")
	public List<PropertiesBackend> getPropertiesBackend(@Param("appName") String appName);

	@Query(value = "select a from PropertiesBackend a where a.appName like concat('%',:appName,'%') and a.name= :name")
	public PropertiesBackend getProperty(@Param("appName") String appName, @Param("name") String name);

}