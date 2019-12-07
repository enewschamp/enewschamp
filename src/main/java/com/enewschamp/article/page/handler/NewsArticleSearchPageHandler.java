package com.enewschamp.article.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.NewsArticleSearchPageData;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.article.page.data.NewsArticleSearchResultData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
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

<<<<<<< Updated upstream
=======
	@Autowired
	EnewschampApplicationProperties appConfig;

>>>>>>> Stashed changes
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {

		PageDTO pageDTO = new PageDTO();
		NewsArticleSearchRequest searchRequestData = null;
		try {
			searchRequestData = objectMapper.readValue(pageRequest.getData().toString(),
					NewsArticleSearchRequest.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		NewsArticleSearchResultData searchResult = new NewsArticleSearchResultData();
		pageDTO.setData(searchResult);

		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
				pageRequest.getHeader());
<<<<<<< Updated upstream

=======
		if (pageResult.getNumberOfElements() > appConfig.getMaxSearchResultsForPublisher()) {
			throw new BusinessException(ErrorCodes.MAX_SEARCH_LIMIT_EXCEEDED,
					"Search results excceeds max allowed records. Please narrow down your search.");
		}
>>>>>>> Stashed changes
		HeaderDTO header = new HeaderDTO();
		// header.setIsLastPage(pageResult.isLast());
		// header.setPageCount(pageResult.getTotalPages());
		// header.setRecordCount(pageResult.getNumberOfElements());
		// header.setPageNo(pageResult.getNumber() + 1);
		pageDTO.setHeader(header);

		searchResult.setNewsArticles(pageResult.getContent());
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
		page.setData(newsArticleSearchPageData);
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