package com.enewschamp.common.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.common.domain.entity.PropertiesBackend;
import com.enewschamp.publication.domain.common.LOVProjection;

@JaversSpringDataAuditable
interface PropertiesBackendRepository extends JpaRepository<PropertiesBackend, Long> {

	@Cacheable
	@Query(value = "select a.name as id, a.value as name from PropertiesBackend a")
	public List<LOVProjection> getPropertiesLOV();

	@Cacheable
	@Query(value = "select a from PropertiesBackend a where a.appName like concat('%',:appName,'%')")
	public List<PropertiesBackend> getPropertiesBackend(@Param("appName") String appName);

	@Cacheable
	@Query(value = "select a from PropertiesBackend a where a.appName like concat('%',:appName,'%') and a.name= :name")
	public PropertiesBackend getProperty(@Param("appName") String appName, @Param("name") String name);

	@Modifying
	@Transactional
	@Query(value = "truncate table properties_backend", nativeQuery = true)
	public void truncate();

	@Modifying
	@Query(value = "truncate table properties_backend_id_seq", nativeQuery = true)
	public void deleteSequences();

	@Modifying
	@Query(value = "insert into properties_backend_id_seq values(1)", nativeQuery = true)
	public void initializeSequence();

}