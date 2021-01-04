package com.enewschamp.article.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.app.service.NewsArticleGroupHelper;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.NewsArticleGroupPageData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.domain.common.YesNoType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "NewsArticleGroupPageHandler")
public class NewsArticleGroupPageHandler implements IPageHandler {

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

	@Autowired
	NewsArticleService articleService;

	@Autowired
	private NewsArticleGroupService articleGroupService;

	@Autowired
	CityService cityService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDTO = new PageDTO();
		NewsArticleGroupPageData data = new NewsArticleGroupPageData();
		String action = pageRequest.getHeader().getAction();
		String userId = pageRequest.getHeader().getUserId();
		Long articleGroupId = 0L;
		if (pageRequest.getData().get("newsArticleGroupId") != null) {
			articleGroupId = pageRequest.getData().get("newsArticleGroupId").asLong();
		}
		NewsArticleGroupDTO articleGroupDTO = null;
		switch (action) {
		case "SaveNewsArticle":
			try {
				objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
				articleGroupDTO = objectMapper.readValue(pageRequest.getData().toString(), NewsArticleGroupDTO.class);
			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			articleGroupDTO = newsArticleGroupHelper.createArticleGroup(articleGroupDTO);
			break;
		case "GetPreviousComments":
			// do nothing;
			break;
		case "CloseNewsArticle":
			articleGroupDTO = modelMapper.map(articleGroupService.closeNewsArticleGroup(articleGroupId, userId),
					NewsArticleGroupDTO.class);
			break;
		case "ReInstateNewsArticle":
			articleGroupDTO = modelMapper.map(articleGroupService.reinstateNewsArticleGroup(articleGroupId, userId),
					NewsArticleGroupDTO.class);
			break;
		}
		data.setNewsArticleGroup(articleGroupDTO);
		data.setGenreLOV(genreService.getLOV());
		data.setPublisherLOV(userService.getPublisherLOV());
		data.setAuthorLOV(userService.getAuthorLOV());
		data.setEditorLOV(userService.getEditorLOV());
		data.setMonthsLOV(MonthType.getLOV());
		data.setDaysLOV(WeekDayType.getLOV());
		data.setEditionsLOV(editionService.getLOV());
		data.setArticleTypeLOV(ArticleType.getLOV());
		data.setIsLinked(YesNoType.getLOV());
		data.setCityLOV(cityService.getLOVForNewsEvents());
		pageDTO.setHeader(pageRequest.getHeader());
		pageDTO.setData(data);
		return pageDTO;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		Long newsArticleGroupId = null;
		if (!"CreateNewsArticle".equalsIgnoreCase(pageNavigationContext.getActionName())
				&& pageNavigationContext.getPageRequest().getData().get("newsArticleGroupId") == null) {
			throw new BusinessException(ErrorCodeConstants.ARTICLE_GROUP_ID_MISSING);
		}
		if (!"CreateNewsArticle".equalsIgnoreCase(pageNavigationContext.getActionName())) {
			newsArticleGroupId = pageNavigationContext.getPageRequest().getData().get("newsArticleGroupId").asLong();
		}
		NewsArticleGroupPageData data = new NewsArticleGroupPageData();
		data.setGenreLOV(genreService.getLOV());
		data.setPublisherLOV(userService.getPublisherLOV());
		data.setAuthorLOV(userService.getAuthorLOV());
		data.setEditorLOV(userService.getEditorLOV());
		data.setMonthsLOV(MonthType.getLOV());
		data.setDaysLOV(WeekDayType.getLOV());
		data.setEditionsLOV(editionService.getLOV());
		data.setArticleTypeLOV(ArticleType.getLOV());
		data.setIsLinked(YesNoType.getLOV());
		data.setCityLOV(cityService.getLOVForNewsEvents());
		if (newsArticleGroupId != null) {
			data.setNewsArticleGroup(newsArticleGroupHelper.getArticleGroup(newsArticleGroupId));
		}
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		pageDTO.setData(data);
		return pageDTO;
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