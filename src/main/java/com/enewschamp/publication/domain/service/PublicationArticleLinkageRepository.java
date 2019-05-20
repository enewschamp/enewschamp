package com.enewschamp.publication.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.publication.domain.entity.PublicationArticleLinkage;
import com.enewschamp.publication.domain.entity.PublicationArticleLinkageKey;

@JaversSpringDataAuditable
public interface PublicationArticleLinkageRepository extends JpaRepository<PublicationArticleLinkage, PublicationArticleLinkageKey>{ 

	@Query(value="SELECT a FROM PublicationArticleLinkage a"
			+ " where a.publicationArticleLinkageKey.publicationId = :publicationId")
	public List<PublicationArticleLinkage> findByPublicationId(long publicationId);
} 