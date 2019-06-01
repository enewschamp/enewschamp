package com.enewschamp.article.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.app.service.NewsArticleGroupHelper;
import com.enewschamp.article.page.data.NewsArticleGroupPageData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value="NewsArticleGroupPageHandler")
public class NewsArticleGroupPageHandler implements IPageHandler  {

	
	@Autowired
	private NewsArticleGroupHelper newsArticleGroupHelper;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	UserService userService;
	
	@Autowired
	EditionService editionService;
	
	@Autowired
	GenreService genreService;
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		
		PageDTO pageDTO = new PageDTO();

		NewsArticleGroupDTO articleGroupDTO = null;
		try {
			articleGroupDTO = objectMapper.readValue(pageRequest.getData().toString(), NewsArticleGroupDTO.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		articleGroupDTO = newsArticleGroupHelper.createArticleGroup(articleGroupDTO);

		NewsArticleGroupPageData data = new NewsArticleGroupPageData();
		data.setNewsArticleGroup(articleGroupDTO);
		pageDTO.setData(data);
		
		return pageDTO;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();

		Long articleGroupId = null;
		articleGroupId = pageNavigationContext.getPageRequest().getData().get("newsArticleGroupId").asLong();
		
		NewsArticleGroupPageData data = new NewsArticleGroupPageData();
		
		data.setGenreLOV(genreService.getLOV());
		data.setPublisherLOV(userService.getPublisherLOV());
		data.setAuthorLOV(userService.getAuthorLOV());
		data.setEditorLOV(userService.getEditorLOV());
		data.setMonthsLOV(MonthType.getLOV());
		data.setDaysLOV(WeekDayType.getLOV());
		data.setEditionsLOV(editionService.getLOV());
		
		data.setNewsArticleGroup(newsArticleGroupHelper.getArticleGroup(articleGroupId));
		
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