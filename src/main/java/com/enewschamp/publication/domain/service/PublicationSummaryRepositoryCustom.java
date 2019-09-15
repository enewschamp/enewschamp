package com.enewschamp.publication.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.publication.app.dto.PublicationDailySummaryDTO;
import com.enewschamp.publication.app.dto.PublicationMonthlySummaryDTO;
import com.enewschamp.publication.page.data.PublicationSummaryRequest;

public interface PublicationSummaryRepositoryCustom {
	public Page<PublicationDailySummaryDTO> fetchDailySummary(PublicationSummaryRequest summaryRequest, Pageable pageable);
	public Page<PublicationMonthlySummaryDTO> fetchMonthlySummary(PublicationSummaryRequest summaryRequest, Pageable pageable);

}
