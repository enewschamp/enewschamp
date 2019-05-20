package com.enewschamp.publication.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.domain.service.PublicationRepositoryCustom;
import com.enewschamp.publication.page.data.PublicationSearchRequest;
import com.enewschamp.publication.page.data.PublicationSearchResultData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value="PublicationSearchPageHandler")
public class PublicationSearchPageHandler implements IPageHandler  {

	@Autowired
	private PublicationRepositoryCustom publicationCustomRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		
		PageDTO pageDTO = new PageDTO();
		PublicationSearchRequest searchRequestData = null;
		try {
			searchRequestData = objectMapper.readValue(pageRequest.getData().toString(), PublicationSearchRequest.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		PublicationSearchResultData searchResult = new PublicationSearchResultData();
		pageDTO.setData(searchResult);
		
		Pageable pageable = PageRequest.of(pageRequest.getHeader().getPageNumber(), pageRequest.getHeader().getPageSize());
		Page<PublicationDTO> pageResult = publicationCustomRepository.findAllPage(searchRequestData, pageable);
		
		HeaderDTO header = new HeaderDTO();
		header.setLastPage(pageResult.isLast());
		header.setPageCount(pageResult.getTotalPages());
		header.setRecordCount(pageResult.getNumberOfElements());
		pageDTO.setHeader(header);
		
		searchResult.setPublications(pageResult.getContent());
		return pageDTO;
	}
}
