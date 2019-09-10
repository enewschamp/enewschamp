package com.enewschamp.domain.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.domain.entity.MultiLanguageText;

@JaversSpringDataAuditable
public interface MultiLanguageTextRepository extends JpaRepository<MultiLanguageText, Long>{ 
	 
} 