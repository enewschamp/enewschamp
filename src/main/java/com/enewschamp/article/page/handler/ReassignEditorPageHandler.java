package com.enewschamp.article.page.handler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.page.data.ReassignEditorPageData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.domain.service.PublicationService;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value="ReassignAuthorPageHandler")
public class ReassignEditorPageHandler implements IPageHandler  {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NewsArticleGroupService articleGroupService;
	
	@Autowired
	private PublicationService publicationService;
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		
		PageDTO pageDTO = new PageDTO();
		String editorId = pageRequest.getData().get("editorId").asText();
		
		// Handle editor assignment from article group page
		Long articleGroupId = pageRequest.getData().get("newsArticleGroupId").asLong();
		if(articleGroupId != null) {
			articleGroupService.assignEditor(articleGroupId, editorId);
		}
		
		Long publicationId = pageRequest.getData().get("publicationId").asLong();
		if(publicationId != null) {
			publicationService.assignEditor(publicationId, editorId);
		}
		
		return pageDTO;
	}
	
	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO page = new PageDTO();
		
		ReassignEditorPageData pageData = new ReassignEditorPageData();
		pageData.setEditorLOV(userService.getEditorLOV());
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