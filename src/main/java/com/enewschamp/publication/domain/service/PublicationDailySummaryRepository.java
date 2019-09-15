package com.enewschamp.publication.domain.service;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.publication.domain.entity.PublicationDailySummary;

public interface PublicationDailySummaryRepository extends JpaRepository<PublicationDailySummary, LocalDate>{ 
	 
	
} 