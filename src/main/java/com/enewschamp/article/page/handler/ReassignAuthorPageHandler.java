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
import com.enewschamp.article.page.data.ReassignAuthorPageData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "ReassignAuthorPageHandler")
public class ReassignAuthorPageHandler implements IPageHandler {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private NewsArticleGroupService articleGroupService;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {

		PageDTO pageDTO = new PageDTO();
		String authorId = pageRequest.getData().get("authorId").asText();
		Long articleGroupId = pageRequest.getData().get("newsArticleGroupId").asLong();

		NewsArticleGroupPageData data = new NewsArticleGroupPageData();
		NewsArticleGroupDTO groupDTO = articleGroupService.assignAuthor(articleGroupId, authorId);
		data.setNewsArticleGroup(groupDTO);
		pageDTO.setData(data);
		return pageDTO;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO page = new PageDTO();
		ReassignAuthorPageData pageData = new ReassignAuthorPageData();
		pageData.setAuthorLOV(userService.getAuthorLOV());
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