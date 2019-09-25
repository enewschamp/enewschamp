package com.enewschamp.publication.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.publication.domain.service.PublicationService;
import com.enewschamp.publication.page.data.PublicationSearchPageData;
import com.enewschamp.publication.page.data.PublicationSearchRequest;
import com.enewschamp.publication.page.data.PublicationSearchResultData;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value="PublicationSearchPageHandler")
public class PublicationSearchPageHandler implements IPageHandler  {

	@Autowired
	private PublicationService publicationService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	GenreService genreService;

	@Autowired
	UserService userService;
	
	@Autowired
	EditionService editionService;
	
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
		
		Page<PublicationDTO> pageResult = publicationService.findPublications(searchRequestData, pageRequest.getHeader());
		
		HeaderDTO header = new HeaderDTO();
		header.setIsLastPage(pageResult.isLast());
		header.setPageCount(pageResult.getTotalPages());
		header.setRecordCount(pageResult.getNumberOfElements());
		header.setPageNo(pageResult.getNumber() + 1);
		pageDTO.setHeader(header);
		
		searchResult.setPublications(pageResult.getContent());
		return pageDTO;
	}
	
	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO page = new PageDTO();
		PublicationSearchPageData publicationSearchPageData = new PublicationSearchPageData();
		publicationSearchPageData.setPublisherLOV(userService.getPublisherLOV());
		publicationSearchPageData.setAuthorLOV(userService.getAuthorLOV());
		publicationSearchPageData.setEditorLOV(userService.getEditorLOV());
		publicationSearchPageData.setEditionsLOV(editionService.getLOV());
		page.setData(publicationSearchPageData);
		return page;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		// TODO Auto-generated method stub
		return null;
	}
}