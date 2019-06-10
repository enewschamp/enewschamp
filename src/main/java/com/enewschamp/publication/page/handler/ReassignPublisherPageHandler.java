package com.enewschamp.publication.page.handler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.domain.service.PublicationService;
import com.enewschamp.publication.page.data.ReassignPublisherPageData;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value="ReassignPublisherPageHandler")
public class ReassignPublisherPageHandler implements IPageHandler  {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PublicationService publicationService;
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		
		PageDTO pageDTO = new PageDTO();
		String publisherId = pageRequest.getData().get("publisherId").asText();
		Long publicationId = pageRequest.getData().get("publicationId").asLong();
		
		publicationService.assignPublisher(publicationId, publisherId);
		
		return pageDTO;
	}
	
	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO page = new PageDTO();
		
		Long publicationId = pageNavigationContext.getPageRequest().getData().get("publicationId").asLong();
		
		ReassignPublisherPageData pageData = new ReassignPublisherPageData();
		pageData.setPublisherLOV(userService.getPublisherLOV());
		page.setData(pageData);
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