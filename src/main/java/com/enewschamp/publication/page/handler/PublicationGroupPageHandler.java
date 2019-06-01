package com.enewschamp.publication.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.app.dto.PublicationGroupDTO;
import com.enewschamp.publication.app.service.PublicationGroupHelper;
import com.enewschamp.publication.page.data.PublicationGroupPageData;
import com.enewschamp.user.domain.service.UserService;
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
	
	@Autowired
	UserService userService;
	
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
		
		publicationGroupDTO = publicationGroupHelper.createPublicationGroup(publicationGroupDTO);
		
		return pageDTO;
	}
	
	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();

		Long publicationGroupId = pageNavigationContext.getPageRequest().getData().get("publicationGroupId").asLong();
		
		PublicationGroupPageData data = new PublicationGroupPageData();
		
		data.setPublisherLOV(userService.getPublisherLOV());
		data.setEditorLOV(userService.getEditorLOV());
		
		data.setPublicationGroup(publicationGroupHelper.getPublicationGroup(publicationGroupId));		
		pageDTO.setData(data);
		return pageDTO;
	}
	
	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();

		Long publicationGroupId = pageNavigationContext.getPageRequest().getData().get("publicationGroupId").asLong();
		
		PublicationGroupPageData data = new PublicationGroupPageData();
		
		data.setPublisherLOV(userService.getPublisherLOV());
		data.setEditorLOV(userService.getEditorLOV());
		
		data.setPublicationGroup(publicationGroupHelper.getPublicationGroup(publicationGroupId));		
		pageDTO.setData(data);
		return pageDTO;
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
