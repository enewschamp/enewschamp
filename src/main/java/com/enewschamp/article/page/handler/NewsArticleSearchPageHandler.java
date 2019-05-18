package com.enewschamp.article.page.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.service.NewsArticleRepository;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.article.page.data.NewsArticleSearchResultData;
import com.enewschamp.domain.common.IPageHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value="NewsArticleSearchPageHandler")
public class NewsArticleSearchPageHandler implements IPageHandler  {

	
	@Autowired
	private NewsArticleRepository newsArticleRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		
		PageDTO pageDTO = new PageDTO();
		ObjectMapper mapper = new ObjectMapper()
			      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		NewsArticleSearchRequest searchRequestData = null;
		try {
			searchRequestData = mapper.readValue(pageRequest.getData().toString(), NewsArticleSearchRequest.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		NewsArticleSearchResultData searchResult = new NewsArticleSearchResultData();
		
		List<NewsArticle> articles = newsArticleRepository.findAll();
		List<NewsArticleDTO> articleDTOs = new ArrayList<NewsArticleDTO>();
		for(NewsArticle article: articles) {
			NewsArticleDTO articleDTO = modelMapper.map(article, NewsArticleDTO.class);
			articleDTOs.add(articleDTO);
		}
		
		searchResult.setNewsArticles(articleDTOs);
		pageDTO.setData(searchResult);
		
		return pageDTO;
	}
}
