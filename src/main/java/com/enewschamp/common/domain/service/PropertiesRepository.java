package com.enewschamp.common.domain.service;

import java.util.List;
import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.common.domain.entity.Properties;
import com.enewschamp.publication.domain.common.LOVProjection;

@JaversSpringDataAuditable
interface PropertiesRepository extends JpaRepository<Properties, Long> {

	@Query(value = "select a.propertyName as id, a.propertyValue as name from Properties a")
	public List<LOVProjection> getPropertiesLOV();

	@Query(value = "select a from Properties a where a.backendOnly='N'")
	public List<Properties> getProperties();

	@Query(value = "select a from Properties a where a.propertyName= :propertyName")
	public Properties getProperty(@Param("propertyName") String propertyName);

}