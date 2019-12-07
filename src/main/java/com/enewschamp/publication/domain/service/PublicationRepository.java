package com.enewschamp.publication.domain.service;

import java.time.LocalDate;
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

	public List<Publication> findByPublicationGroupId(long publicationGroupId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM Publication a" + " where a.publicationGroupId = :publicationGroupId")
	public void deleteByPublicationGroupId(@Param("publicationGroupId") long publicationGroupId);

	@Query(value = "SELECT a.status FROM Publication a" + " where a.publicationId = :publicationId")
	public PublicationStatusType getCurrentStatus(@Param("publicationId") long publicationId);

	@Query(value = "SELECT a.previousStatus FROM Publication a" + " where a.publicationId = :publicationId")
	public PublicationStatusType getPreviousStatus(@Param("publicationId") long publicationId);

	@Query(value = "SELECT min(a.publishDate) FROM Publication a" + " where a.publishDate > :givenDate"
			+ " and a.editionId = :editionId" + " and a.readingLevel = :readingLevel")
	public LocalDate getNextAvailablePublicationDate(@Param("givenDate") LocalDate givenDate,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel);

	@Query(value = "SELECT max(a.publishDate) FROM Publication a" + " where " + " a.editionId = :editionId"
			+ " and a.readingLevel = :readingLevel")
	public LocalDate getLatestPublicationDate(@Param("editionId") String editionId,
			@Param("readingLevel") int readingLevel);

	@Query(value = "SELECT max(a.publishDate) FROM Publication a" + " where a.publishDate < :givenDate"
			+ " and a.editionId = :editionId" + " and a.readingLevel = :readingLevel")
	public LocalDate getPreviousAvailablePublicationDate(@Param("givenDate") LocalDate givenDate,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel);

}
