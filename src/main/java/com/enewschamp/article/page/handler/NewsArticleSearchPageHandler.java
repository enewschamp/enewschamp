package com.enewschamp.article.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.article.app.dto.PublisherNewsArticleSummaryDTO;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.NewsArticleSearchPageData;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.article.page.data.NewsArticleSearchResultData;
import com.enewschamp.common.domain.service.PropertiesBackendService;
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

@Component(value = "NewsArticleSearchPageHandler")
public class NewsArticleSearchPageHandler implements IPageHandler {

	@Autowired
	private NewsArticleService newsArticleService;

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

	@Autowired
	CityService cityService;

	@Autowired
	PropertiesBackendService propertiesService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDTO = new PageDTO();
		NewsArticleSearchRequest searchRequestData = null;
		try {
			objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
			searchRequestData = objectMapper.readValue(pageRequest.getData().toString(),
					NewsArticleSearchRequest.class);
		} catch (JsonParseException e) {
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, "Json");
		} catch (JsonMappingException e) {
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, "Json");
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, "Json");
		}
		NewsArticleSearchResultData searchResult = new NewsArticleSearchResultData();
		Page<PublisherNewsArticleSummaryDTO> pageResult = newsArticleService.findPublisherArticles(searchRequestData, 1,
				10, pageRequest.getHeader());
		if (pageResult.getNumberOfElements() > Integer.valueOf(propertiesService
				.getValue(pageRequest.getHeader().getModule(), PropertyConstants.MAX_SEARCH_RESULTS_FOR_PUBLISHER))) {
			throw new BusinessException(ErrorCodeConstants.MAX_SEARCH_LIMIT_EXCEEDED);
		}
		searchResult.setNewsArticles(pageResult.getContent());
		pageDTO.setData(searchResult);
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO page = new PageDTO();
		NewsArticleSearchPageData newsArticleSearchPageData = new NewsArticleSearchPageData();
		newsArticleSearchPageData.setGenreLOV(genreService.getLOV());
		newsArticleSearchPageData.setPublisherLOV(userService.getPublisherLOV());
		newsArticleSearchPageData.setAuthorLOV(userService.getAuthorLOV());
		newsArticleSearchPageData.setEditorLOV(userService.getEditorLOV());
		newsArticleSearchPageData.setMonthsLOV(MonthType.getLOV());
		newsArticleSearchPageData.setDaysLOV(WeekDayType.getLOV());
		newsArticleSearchPageData.setEditionsLOV(editionService.getLOV());
		newsArticleSearchPageData.setCityLOV(cityService.getLOVForNewsEvents());
		newsArticleSearchPageData.setArticleTypeLOV(ArticleType.getLOV());
		newsArticleSearchPageData.setIsLinkedLOV(YesNoType.getLOV());
		page.setData(newsArticleSearchPageData);
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