package com.enewschamp.article.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.service.NewsArticleGroupService;

@Component
public class NewsArticleGroupHelper {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private NewsArticleGroupService newsArticleGroupService;

	public NewsArticleGroupDTO createArticleGroup(NewsArticleGroupDTO articleGroupDTO) {
		NewsArticleGroup articleGroup = modelMapper.map(articleGroupDTO, NewsArticleGroup.class);
		articleGroup = newsArticleGroupService.create(articleGroup);
		articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		return articleGroupDTO;
	}

	public NewsArticleGroupDTO getArticleGroup(Long articleGroupId) {
		NewsArticleGroup articleGroup = newsArticleGroupService.get(articleGroupId);
		NewsArticleGroupDTO articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		newsArticleGroupService.get(articleGroupId);
		return articleGroupDTO;
	}
	
}
