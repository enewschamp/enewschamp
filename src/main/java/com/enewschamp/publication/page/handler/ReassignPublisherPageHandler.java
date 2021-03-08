package com.enewschamp.publication.page.handler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.domain.service.PublicationGroupService;
import com.enewschamp.publication.page.data.ReassignPublisherPageData;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "ReassignPublisherPageHandler")
public class ReassignPublisherPageHandler implements IPageHandler {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private PublicationGroupService publicationGroupService;

	@Autowired
	private PublicationGroupPageHandler publicationGroupPageHandler;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		// PageDTO pageDTO = new PageDTO();
		String publisherId = pageRequest.getData().get("publisherId").asText();
		Long publicationGroupId = pageRequest.getData().get("publicationGroupId").asLong();
		String operatorId = pageRequest.getHeader().getUserId();
		// PublicationGroupPageData data = new PublicationGroupPageData();
		// PublicationGroupDTO groupDTO =
		publicationGroupService.assignPublisher(publicationGroupId, publisherId, operatorId);
		PageNavigationContext pagNavContext = new PageNavigationContext();
		pagNavContext.setPageRequest(pageRequest);
		return publicationGroupPageHandler.loadPage(pagNavContext);
		// data.setPublicationGroup(groupDTO);
		// pageDTO.setData(data);
		// return pageDTO;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO page = new PageDTO();
		ReassignPublisherPageData pageData = new ReassignPublisherPageData();
		pageData.setPublisherLOV(userService.getPublisherLOV());
		page.setData(pageData);
		page.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return page;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}
}