package com.enewschamp.article.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.domain.service.NewsArticleRepositoryCustom;
import com.enewschamp.article.page.data.NewsArticleSearchPageData;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.article.page.data.NewsArticleSearchResultData;
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

@Component(value="NewsArticleSearchPageHandler")
public class NewsArticleSearchPageHandler implements IPageHandler  {

	@Autowired
	private NewsArticleRepositoryCustom newsArticleCustomRepository;
	
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
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		
		PageDTO pageDTO = new PageDTO();
		NewsArticleSearchRequest searchRequestData = null;
		try {
			searchRequestData = objectMapper.readValue(pageRequest.getData().toString(), NewsArticleSearchRequest.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		NewsArticleSearchResultData searchResult = new NewsArticleSearchResultData();
		pageDTO.setData(searchResult);
		
		Pageable pageable = PageRequest.of(pageRequest.getHeader().getPageNumber(), pageRequest.getHeader().getPageSize());
		Page<NewsArticleDTO> pageResult = newsArticleCustomRepository.findAllPage(searchRequestData, pageable);
		
		HeaderDTO header = new HeaderDTO();
		header.setLastPage(pageResult.isLast());
		header.setPageCount(pageResult.getTotalPages());
		header.setRecordCount(pageResult.getNumberOfElements());
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
		page.setData(newsArticleSearchPageData);
		return page;
	}
}
