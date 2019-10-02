package com.enewschamp.publication.domain.service;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.publication.domain.common.PublicationStatusType;
import com.enewschamp.publication.domain.entity.Publication;

@JaversSpringDataAuditable
public interface PublicationRepository extends JpaRepository<Publication, Long>{ 
	 
	public	List<Publication> findByPublicationGroupId(long publicationGroupId);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM Publication a"
			+ " where a.publicationGroupId = :publicationGroupId")
	public void deleteByPublicationGroupId(long publicationGroupId);
	
	@Query(value = "SELECT a.status FROM Publication a"
			+ " where a.publicationId = :publicationId")
	public PublicationStatusType getCurrentStatus(long publicationId);
	
	@Query(value = "SELECT a.previousStatus FROM Publication a"
			+ " where a.publicationId = :publicationId")
	public PublicationStatusType getPreviousStatus(long publicationId);
	
	@Query(value = "SELECT min(a.publishDate) FROM Publication a"
			+ " where a.publishDate > :givenDate"
			+ " and a.editionId = :editionId"
			+ " and a.readingLevel = :readingLevel")
	public LocalDate getNextAvailablePublicationDate(LocalDate givenDate, String editionId, int readingLevel);
	
	@Query(value = "SELECT max(a.publishDate) FROM Publication a"
			+ " where "
			+ " a.editionId = :editionId"
			+ " and a.readingLevel = :readingLevel")
	public LocalDate getLatestPublicationDate(String editionId, int readingLevel);
	
	
	@Query(value = "SELECT max(a.publishDate) FROM Publication a"
			+ " where a.publishDate < :givenDate"
			+ " and a.editionId = :editionId"
			+ " and a.readingLevel = :readingLevel")
	public LocalDate getPreviousAvailablePublicationDate(LocalDate givenDate, String editionId, int readingLevel);
	
} 
