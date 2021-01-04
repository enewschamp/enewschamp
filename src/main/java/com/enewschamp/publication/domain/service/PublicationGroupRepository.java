package com.enewschamp.publication.domain.service;

import java.time.LocalDate;
import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.publication.domain.entity.PublicationGroup;

@JaversSpringDataAuditable
public interface PublicationGroupRepository extends JpaRepository<PublicationGroup, Long> {
	@Query(value = "SELECT a FROM PublicationGroup a" + " where a.publicationDate = :publicationDate")
	public Optional<PublicationGroup> getPublicationGroupByPublicationDate(
			@Param("publicationDate") LocalDate publicationDate);

}