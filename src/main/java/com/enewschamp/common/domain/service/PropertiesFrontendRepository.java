package com.enewschamp.common.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.common.domain.entity.PropertiesFrontend;
import com.enewschamp.publication.domain.common.LOVProjection;

@JaversSpringDataAuditable
interface PropertiesFrontendRepository extends JpaRepository<PropertiesFrontend, Long> {

	@Cacheable
	@Query(value = "select a.name as id, a.value as name from PropertiesFrontend a")
	public List<LOVProjection> getPropertiesLOV();

	@Cacheable
	@Query(value = "select a from PropertiesFrontend a where a.appName like concat('%',:appName,'%')")
	public List<PropertiesFrontend> getPropertiesFrontend(@Param("appName") String appName);

	@Cacheable
	@Query(value = "select a from PropertiesFrontend a where a.appName like concat('%',:appName,'%') and a.name= :name")
	public PropertiesFrontend getProperty(@Param("appName") String appName, @Param("name") String name);

}