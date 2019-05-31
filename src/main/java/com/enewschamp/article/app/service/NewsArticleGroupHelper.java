package com.enewschamp.article.app.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.service.NewsArticleGroupRepository;
import com.enewschamp.article.domain.service.NewsArticleGroupService;

@Component
public class NewsArticleGroupHelper {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private NewsArticleGroupService newsArticleGroupService;
	
	@Autowired
	NewsArticleGroupRepository repository;
	
	@Autowired
	private NewsArticleHelper newsArticleHelper;
	
	public NewsArticleGroupDTO createArticleGroup(NewsArticleGroupDTO articleGroupDTO) {
		
		List<NewsArticleDTO> newsArticles = articleGroupDTO.getNewsArticles();
		
		NewsArticleGroup articleGroup = modelMapper.map(articleGroupDTO, NewsArticleGroup.class);
		articleGroup = newsArticleGroupService.create(articleGroup);
		articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		
		//Delete existing records, if any
//		newsArticleHelper.deleteByArticleGroupId(articleGroup.getNewsArticleGroupId());
		
		List<NewsArticleDTO> articleList = new ArrayList<NewsArticleDTO>(); 
		for(NewsArticleDTO articleDTO: newsArticles) {
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
