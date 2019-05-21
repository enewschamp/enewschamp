package com.enewschamp.publication.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.app.dto.PublicationGroupDTO;
import com.enewschamp.publication.app.service.PublicationGroupHelper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value="PublicationGroupPageHandler")
public class PublicationGroupPageHandler implements IPageHandler  {

	
	@Autowired
	private PublicationGroupHelper publicationGroupHelper;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		
		PageDTO pageDTO = new PageDTO();

		PublicationGroupDTO publicationGroupDTO = null;
		try {
			publicationGroupDTO = objectMapper.readValue(pageRequest.getData().toString(), PublicationGroupDTO.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		publicationGroupHelper.createPublicationGroup(publicationGroupDTO);
		
		return pageDTO;
	}
	
	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		return pageNavigationContext.getPreviousPageResponse();
	}
}
