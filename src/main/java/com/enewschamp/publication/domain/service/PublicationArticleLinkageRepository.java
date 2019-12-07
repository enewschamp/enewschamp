package com.enewschamp.publication.domain.service;

import java.util.List;

import javax.transaction.Transactional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.publication.domain.entity.PublicationArticleLinkage;

@JaversSpringDataAuditable
public interface PublicationArticleLinkageRepository extends JpaRepository<PublicationArticleLinkage, Long> {

	@Query(value = "SELECT a FROM PublicationArticleLinkage a" + " where a.publicationId = :publicationId")
	public List<PublicationArticleLinkage> findByPublicationId(@Param("publicationId") long publicationId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM PublicationArticleLinkage a" + " where a.publicationId = :publicationId")
	public void deleteByPublicationId(@Param("publicationId") long publicationId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM PublicationArticleLinkage a" + " where a.linkageId = :linkageId")
	public void deleteById(@Param("linkageId") long linkageId);
}