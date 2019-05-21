package com.enewschamp.article.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.app.service.NewsArticleGroupHelper;
import com.enewschamp.domain.common.IPageHandler;
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
		
		newsArticleGroupHelper.createArticleGroup(articleGroupDTO);
		
		return pageDTO;
	}
}
