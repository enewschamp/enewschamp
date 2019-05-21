package com.enewschamp.article.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.service.NewsArticleGroupRepository;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;

@Component
public class NewsArticleGroupHelper {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private NewsArticleGroupService newsArticleGroupService;
	
	@Autowired
	NewsArticleGroupRepository repository;
	
	@Autowired
	private NewsArticleService newsArticleService;

	@Autowired
	private NewsArticleHelper newsArticleHelper;
	
	public NewsArticleGroupDTO createArticleGroup(NewsArticleGroupDTO articleGroupDTO) {
		
		List<NewsArticleDTO> newsArticles = articleGroupDTO.getNewsArticles();
		
		NewsArticleGroup articleGroup = modelMapper.map(articleGroupDTO, NewsArticleGroup.class);
		articleGroup = newsArticleGroupService.create(articleGroup);
		articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		
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
