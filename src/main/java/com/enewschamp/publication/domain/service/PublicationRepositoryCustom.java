package com.enewschamp.publication.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.publication.app.dto.PublicationSummaryDTO;
import com.enewschamp.publication.page.data.PublicationSearchRequest;

public interface PublicationRepositoryCustom {
	public Page<PublicationSummaryDTO> findPublications(PublicationSearchRequest searchRequest, Pageable pageable);
}
