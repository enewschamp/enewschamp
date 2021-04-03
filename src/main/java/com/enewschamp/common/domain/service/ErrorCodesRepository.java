package com.enewschamp.common.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.common.domain.entity.ErrorCodes;
import com.enewschamp.publication.domain.common.LOVProjection;

@JaversSpringDataAuditable
interface ErrorCodesRepository extends JpaRepository<ErrorCodes, Long> {

	@Query(value = "select a.errorCode as id, a.errorMessage as name from ErrorCodes a")
	public List<LOVProjection> getErrorCodesLOV();

	@Query(value = "select a from ErrorCodes a where a.errorCode= :errorCode")
	public ErrorCodes getErrorCodes(@Param("errorCode") String errorCode);
	
	@Modifying
	@Query(value = "truncate table error_codes", nativeQuery = true)
	public void truncate();
	
	@Modifying
	@Query(value = "truncate table error_code_id_seq", nativeQuery = true)
	public void deleteSequences();
	
	@Modifying
	@Query(value = "insert into error_code_id_seq values(1)", nativeQuery = true)
	public void initializeSequence();
}