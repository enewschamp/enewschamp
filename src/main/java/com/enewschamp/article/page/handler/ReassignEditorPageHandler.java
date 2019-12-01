package com.enewschamp.article.page.handler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.page.data.NewsArticleGroupPageData;
import com.enewschamp.article.page.data.ReassignEditorPageData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.app.dto.PublicationGroupDTO;
import com.enewschamp.publication.domain.service.PublicationService;
import com.enewschamp.publication.page.data.PublicationGroupPageData;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "ReassignEditorPageHandler")
public class ReassignEditorPageHandler implements IPageHandler {

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
		Long articleGroupId = null;
		JsonNode node = pageRequest.getData().get("newsArticleGroupId");
		if (node != null) {
			articleGroupId = node.asLong();
		}
		if (articleGroupId != null) {
			NewsArticleGroupPageData data = new NewsArticleGroupPageData();
			NewsArticleGroupDTO groupDTO = modelMapper.map(articleGroupService.assignEditor(articleGroupId, editorId),
					NewsArticleGroupDTO.class);
			data.setNewsArticleGroup(groupDTO);
			pageDTO.setData(data);
		}

		Long publicationGroupId = null;
		node = pageRequest.getData().get("publicationGroupId");
		if (node != null) {
			publicationGroupId = node.asLong();
		}
		if (publicationGroupId != null) {
			PublicationGroupPageData data = new PublicationGroupPageData();
			PublicationGroupDTO groupDTO = modelMapper
					.map(publicationService.assignEditor(publicationGroupId, editorId), PublicationGroupDTO.class);
			data.setPublicationGroup(groupDTO);
			pageDTO.setData(data);
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