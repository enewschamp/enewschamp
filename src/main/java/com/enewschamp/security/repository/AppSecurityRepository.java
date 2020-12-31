package com.enewschamp.security.repository;

import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.security.entity.AppSecurity;
@JaversSpringDataAuditable
public interface AppSecurityRepository extends JpaRepository<AppSecurity, Long> {

	@Query("Select a from AppSecurity a where a.appName = :appName and a.appKey = :appKey and a.module= :module")
	public Optional<AppSecurity> getAppSec(@Param("appName") String appName, @Param("appKey") String appKey,
			@Param("module") String module);
}
