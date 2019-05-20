package com.enewschamp.domain.service;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.domain.entity.MultiLanguageText;

@JaversSpringDataAuditable
interface MultiLanguageTextRepository extends JpaRepository<MultiLanguageText, Long>{ 
	 
} 