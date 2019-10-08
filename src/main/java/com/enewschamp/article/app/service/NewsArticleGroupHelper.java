package com.enewschamp.article.app.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.domain.common.ArticleActionType;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.service.NewsArticleGroupRepository;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleRepository;
import com.enewschamp.domain.common.RecordInUseType;

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
		
		List<NewsArticleDTO> newsArticles = null;
		NewsArticleGroup existingArticleGroup = newsArticleGroupService.get(articleGroupDTO.getNewsArticleGroupId());
		if(existingArticleGroup != null || articleGroupDTO.getNewsArticles() != null) {
			//checkForArticleIds(articleGroupDTO, existingArticleGroup);
			newsArticles = articleGroupDTO.getNewsArticles();
		} else {
			newsArticles = createDefaultNewsArticles(articleGroupDTO);
		}
		
		NewsArticleGroup articleGroup = modelMapper.map(articleGroupDTO, NewsArticleGroup.class);
		
		List<NewsArticle> articles = new ArrayList<NewsArticle>();
		for(NewsArticleDTO articleDTO: newsArticles) {
			articles.add(modelMapper.map(articleDTO, NewsArticle.class));
		}
		articleGroup.setNewsArticles(articles);
		
		articleGroup = newsArticleGroupService.create(articleGroup);
		articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		
		List<NewsArticleDTO> articleList = new ArrayList<NewsArticleDTO>(); 
		for(NewsArticleDTO articleDTO: newsArticles) {
			articleDTO.setNewsArticleGroupId(articleGroup.getNewsArticleGroupId());
			articleDTO.setOperatorId(articleGroup.getOperatorId());
			if(articleGroupDTO.isReadingLevelPresent(articleDTO.getReadingLevel()) ) {
				articleDTO.setRecordInUse(articleGroup.getRecordInUse());
			} else {
				articleDTO.setRecordInUse(RecordInUseType.N);
				articleDTO.setCurrentAction(ArticleActionType.Close);
			}
			articleDTO = newsArticleHelper.create(articleDTO);
			articleList.add(articleDTO);
		}
		articleGroupDTO.setNewsArticles(articleList);
		
		articleGroupDTO.setBase64Image(null);
		return articleGroupDTO;
	}
	
	
	private List<NewsArticleDTO> createDefaultNewsArticles(NewsArticleGroupDTO articleGroupDTO) {
		
		List<NewsArticleDTO> newsArticles = new ArrayList<NewsArticleDTO>();
		if(articleGroupDTO.getReadingLevel1() != null && articleGroupDTO.getReadingLevel1()) {
			newsArticles.add(createDefaultNewsArticle(articleGroupDTO, 1));
		}
		if(articleGroupDTO.getReadingLevel2() != null && articleGroupDTO.getReadingLevel2()) {
			newsArticles.add(createDefaultNewsArticle(articleGroupDTO, 2));
		}
		if(articleGroupDTO.getReadingLevel3() != null && articleGroupDTO.getReadingLevel3()) {
			newsArticles.add(createDefaultNewsArticle(articleGroupDTO, 3));
		}
		if(articleGroupDTO.getReadingLevel4() != null && articleGroupDTO.getReadingLevel4()) {
			newsArticles.add(createDefaultNewsArticle(articleGroupDTO, 4));
		}
		return newsArticles;
	}
	
	private NewsArticleDTO createDefaultNewsArticle(NewsArticleGroupDTO articleGroupDTO, int readingLevel) {
		NewsArticleDTO article = new NewsArticleDTO();
		article.setAuthorId(articleGroupDTO.getAuthorId());
		article.setRecordInUse(RecordInUseType.Y);
		article.setOperationDateTime(articleGroupDTO.getOperationDateTime());
		article.setReadingLevel(readingLevel);
		return article;
	}

	public NewsArticleGroupDTO getArticleGroup(Long articleGroupId) {
		NewsArticleGroup articleGroup = newsArticleGroupService.load(articleGroupId);
		NewsArticleGroupDTO articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		
		List<NewsArticleDTO> articles = newsArticleHelper.getByArticleGroupId(articleGroupId);
		articleGroupDTO.setNewsArticles(articles);
		
		return articleGroupDTO;
	}
	
//	private void checkForArticleIds(NewsArticleGroupDTO articleGroupDTO, NewsArticleGroup articleGroup) {
//		List<Long> newArticleIds = new ArrayList<Long>();
//		for(NewsArticleDTO newsArticle : articleGroupDTO.getNewsArticles()) {
//			if(newsArticle.getNewsArticleId() <= 0) {
//				throw new BusinessException(ErrorCodes.INVALID_ARTICLE_ID);
//			}
//			newArticleIds.add(newsArticle.getNewsArticleId());
//		}
//		
//		List<NewsArticle> existingArticles = newsArticleRepository.findByNewsArticleGroupId(articleGroupDTO.getNewsArticleGroupId());
//		
//		existingArticles.forEach(existingArticle -> {
//			if(!newArticleIds.contains(existingArticle.getNewsArticleId())) {
//				throw new BusinessException(ErrorCodes.ARTICLE_ID_CHANGED);
//			}
//		});
//	}
	
}