package com.enewschamp.publication.domain.service;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.publication.domain.entity.HashTag;

@JaversSpringDataAuditable
interface HashTagRepository extends JpaRepository<HashTag, String>{ 
	 
} 