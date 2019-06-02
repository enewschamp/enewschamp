package com.enewschamp.article.app.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.service.NewsArticleGroupRepository;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleRepository;
import com.enewschamp.problem.BusinessException;

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
	
	@Autowired
	private NewsArticleRepository newsArticleRepository;
	
	public NewsArticleGroupDTO createArticleGroup(NewsArticleGroupDTO articleGroupDTO) {
		
		checkForArticleIds(articleGroupDTO);
		
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
		NewsArticleGroup articleGroup = newsArticleGroupService.load(articleGroupId);
		NewsArticleGroupDTO articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		
		List<NewsArticleDTO> articles = newsArticleHelper.getByArticleGroupId(articleGroupId);
		articleGroupDTO.setNewsArticles(articles);
		
		return articleGroupDTO;
	}
	
	private void checkForArticleIds(NewsArticleGroupDTO articleGroupDTO) {
		
		NewsArticleGroup articleGroup = newsArticleGroupService.get(articleGroupDTO.getNewsArticleGroupId());
		if(articleGroup != null) {
			
			List<Long> newArticleIds = new ArrayList<Long>();
			for(NewsArticleDTO newsArticle : articleGroupDTO.getNewsArticles()) {
				if(newsArticle.getNewsArticleId() <= 0) {
					throw new BusinessException(ErrorCodes.INVALID_ARTICLE_ID);
				}
				newArticleIds.add(newsArticle.getNewsArticleId());
			}
			
			List<NewsArticle> existingArticles = newsArticleRepository.findByNewsArticleGroupId(articleGroupDTO.getNewsArticleGroupId());
			
			existingArticles.forEach(existingArticle -> {
				if(!newArticleIds.contains(existingArticle.getNewsArticleId())) {
					throw new BusinessException(ErrorCodes.ARTICLE_ID_CHANGED);
				}
			});
			
		}
		
	}
	
}