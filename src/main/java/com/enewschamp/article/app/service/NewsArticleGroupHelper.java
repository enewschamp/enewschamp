package com.enewschamp.article.app.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleService;

@Component
public class NewsArticleGroupHelper {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private NewsArticleGroupService newsArticleGroupService;
	
	@Autowired
	private NewsArticleService newsArticleService;

	@Autowired
	private NewsArticleHelper newsArticleHelper;
	
	public NewsArticleGroupDTO createArticleGroup(NewsArticleGroupDTO articleGroupDTO) {
		NewsArticleGroup articleGroup = modelMapper.map(articleGroupDTO, NewsArticleGroup.class);
		articleGroup = newsArticleGroupService.create(articleGroup);
		articleGroupDTO.setNewsArticleGroupId(articleGroup.getNewsArticleGroupId());
		
		List<NewsArticleDTO> articleList = new ArrayList<NewsArticleDTO>(); 
		for(NewsArticleDTO articleDTO: articleGroupDTO.getNewsArticles()) {
			articleDTO.setNewsArticleGroupId(articleGroup.getNewsArticleGroupId());
			articleDTO.setRecordInUse(articleGroup.getRecordInUse());
			articleDTO.setOperatorId(articleGroup.getOperatorId());
			articleDTO = newsArticleHelper.create(articleDTO);
			articleList.add(articleDTO);
		}
		articleGroupDTO.setNewsArticles(articleList);
		
		return articleGroupDTO;
	}

	public NewsArticleGroupDTO getArticleGroup(Long articleGroupId) {
		NewsArticleGroup articleGroup = newsArticleGroupService.get(articleGroupId);
		NewsArticleGroupDTO articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		
		List<NewsArticleDTO> articles = newsArticleHelper.getByArticleGroupId(articleGroupId);
		articleGroupDTO.setNewsArticles(articles);
		
		return articleGroupDTO;
	}
	
}
