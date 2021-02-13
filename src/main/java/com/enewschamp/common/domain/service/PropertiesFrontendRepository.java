package com.enewschamp.common.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.common.domain.entity.PropertiesFrontend;
import com.enewschamp.publication.domain.common.LOVProjection;

@JaversSpringDataAuditable
interface PropertiesFrontendRepository extends JpaRepository<PropertiesFrontend, Long> {

	@Query(value = "select a.name as id, a.value as name from PropertiesFrontend a")
	public List<LOVProjection> getPropertiesLOV();

	@Query(value = "select a from PropertiesFrontend a where a.appName like concat('%',:appName,'%')")
	public List<PropertiesFrontend> getPropertiesFrontend(@Param("appName") String appName);

	@Query(value = "select a from PropertiesFrontend a where a.appName like concat('%',:appName,'%') and a.name= :name")
	public PropertiesFrontend getProperty(@Param("appName") String appName, @Param("name") String name);
	
	@Modifying
	@Transactional
	@Query(value = "truncate table properties_frontend", nativeQuery = true)
	public void truncate();
	
	@Modifying
	@Query(value = "truncate table properties_frontend_id_seq", nativeQuery = true)
	public void deleteSequences();
	
	@Modifying
	@Query(value = "insert into properties_frontend_id_seq values(1)", nativeQuery = true)
	public void initializeSequence();

}