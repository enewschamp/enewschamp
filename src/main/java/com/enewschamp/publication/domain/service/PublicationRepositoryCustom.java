package com.enewschamp.publication.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.page.data.PublicationSearchRequest;

public interface PublicationRepositoryCustom {
	public Page<PublicationDTO> findPublications(PublicationSearchRequest searchRequest, Pageable pageable);

}
