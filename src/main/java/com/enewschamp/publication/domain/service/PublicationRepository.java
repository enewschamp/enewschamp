package com.enewschamp.publication.domain.service;

import java.util.List;

import javax.transaction.Transactional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.publication.domain.common.PublicationStatusType;
import com.enewschamp.publication.domain.entity.Publication;

@JaversSpringDataAuditable
public interface PublicationRepository extends JpaRepository<Publication, Long> {

	@Query(value = "SELECT a FROM Publication a" + " where a.publicationGroupId = :publicationGroupId")
	public List<Publication> findByPublicationGroupId(@Param("publicationGroupId") long publicationGroupId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM Publication a" + " where a.publicationGroupId = :publicationGroupId")
	public void deleteByPublicationGroupId(@Param("publicationGroupId") long publicationGroupId);

	@Query(value = "SELECT a.status FROM Publication a" + " where a.publicationId = :publicationId")
	public PublicationStatusType getCurrentStatus(@Param("publicationId") long publicationId);

	@Query(value = "SELECT a.previousStatus FROM Publication a" + " where a.publicationId = :publicationId")
	public PublicationStatusType getPreviousStatus(@Param("publicationId") long publicationId);
}
