package com.enewschamp.publication.domain.service;

import java.util.List;

import javax.transaction.Transactional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.publication.domain.entity.Publication;

@JaversSpringDataAuditable
public interface PublicationRepository extends JpaRepository<Publication, Long>{ 
	 
	public	List<Publication> findByPublicationGroupId(long publicationGroupId);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM Publication a"
			+ " where a.publicationGroupId = :publicationGroupId")
	public void deleteByPublicationGroupId(long publicationGroupId);
} 