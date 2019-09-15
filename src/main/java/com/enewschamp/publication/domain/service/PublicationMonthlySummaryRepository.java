package com.enewschamp.publication.domain.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.publication.domain.entity.PublicationMonthlySummary;

public interface PublicationMonthlySummaryRepository extends JpaRepository<PublicationMonthlySummary, String>{ 
	 
	
} 