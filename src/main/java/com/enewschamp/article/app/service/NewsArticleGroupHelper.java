package com.enewschamp.article.app.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.domain.common.ArticleActionType;
import com.enewschamp.article.domain.common.ArticleGroupStatusType;
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
		if (existingArticleGroup != null || articleGroupDTO.getNewsArticles() != null) {
			newsArticles = getNewsArticles(articleGroupDTO);
		} else {
			newsArticles = createDefaultNewsArticles(articleGroupDTO);
		}
		NewsArticleGroup articleGroup = modelMapper.map(articleGroupDTO, NewsArticleGroup.class);
		if (articleGroup.getRecordInUse() == null) {
			articleGroup.setRecordInUse(RecordInUseType.Y);
		}

		List<NewsArticle> articles = new ArrayList<NewsArticle>();
		for (NewsArticleDTO articleDTO : newsArticles) {
			articles.add(modelMapper.map(articleDTO, NewsArticle.class));
		}
		articleGroup.setNewsArticles(articles);
		if (articleGroup.getNewsArticleGroupId() != null && articleGroup.getNewsArticleGroupId() > 0) {
			articleGroup = newsArticleGroupService.update(articleGroup);
		} else {
			articleGroup = newsArticleGroupService.create(articleGroup);
		}
		articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);

		List<NewsArticleDTO> articleList = new ArrayList<NewsArticleDTO>();
		for (NewsArticleDTO articleDTO : newsArticles) {
			NewsArticleDTO existingArticleDTO = null;
			if (articleDTO.getNewsArticleId() != null) {
				existingArticleDTO = newsArticleHelper.get(articleDTO.getNewsArticleId());
			}
			if (existingArticleDTO == null || (articleDTO.getCurrentAction() != null)) {
				if (existingArticleDTO != null) {
					articleDTO.setStatus(existingArticleDTO.getStatus());
					articleDTO.setLikeLCount(existingArticleDTO.getLikeLCount());
					articleDTO.setLikeHCount(existingArticleDTO.getLikeHCount());
					articleDTO.setLikeOCount(existingArticleDTO.getLikeOCount());
					articleDTO.setLikeWCount(existingArticleDTO.getLikeWCount());
					articleDTO.setLikeSCount(existingArticleDTO.getLikeSCount());
					articleDTO.setPublicationDate(existingArticleDTO.getPublicationDate());
					articleDTO.setPublicationId(existingArticleDTO.getPublicationId());
					articleDTO.setSequence(existingArticleDTO.getSequence());
				}
				articleDTO.setNewsArticleGroupId(articleGroup.getNewsArticleGroupId());
				articleDTO.setOperatorId(articleGroup.getOperatorId());
				if (articleGroupDTO.isReadingLevelPresent(articleDTO.getReadingLevel())) {
					articleDTO.setRecordInUse(articleGroup.getRecordInUse());
				} else {
					articleDTO.setRecordInUse(RecordInUseType.N);
					articleDTO.setCurrentAction(ArticleActionType.Close);
				}
				articleDTO = copyFromArticleGroup(articleGroupDTO, articleDTO);
				articleDTO = newsArticleHelper.create(articleDTO);
			} else {
				articleDTO = existingArticleDTO;
			}
			articleList.add(articleDTO);
		}
		articleGroup = newsArticleGroupService.get(articleGroupDTO.getNewsArticleGroupId());
		articles = newsArticleRepository.findByNewsArticleGroupId(articleGroup.getNewsArticleGroupId());

		ArticleGroupStatusType newStatus = newsArticleGroupService.deriveNewStatus(articles);
		if (ArticleGroupStatusType.Closed.equals(newStatus)) {
			articleGroup.setRecordInUse(RecordInUseType.N);
		}
		articleGroup.setStatus(newStatus);
		articleGroup.setNewsArticles(articles);
		articleGroup = repository.save(articleGroup);
		articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		articleGroupDTO.setNewsArticles(articleList);
		articleGroupDTO.setBase64Image(null);
		return articleGroupDTO;
	}

	private NewsArticleDTO copyFromArticleGroup(NewsArticleGroupDTO articleGroupDTO, NewsArticleDTO articleDTO) {
		articleDTO.setNewsArticleGroupId(articleGroupDTO.getNewsArticleGroupId());
		return articleDTO;
	}

	private List<NewsArticleDTO> createDefaultNewsArticles(NewsArticleGroupDTO articleGroupDTO) {
		List<NewsArticleDTO> newsArticles = new ArrayList<NewsArticleDTO>();
		if (articleGroupDTO.getReadingLevel1() != null && articleGroupDTO.getReadingLevel1()) {
			newsArticles.add(createDefaultNewsArticle(articleGroupDTO, 1));
		}
		if (articleGroupDTO.getReadingLevel2() != null && articleGroupDTO.getReadingLevel2()) {
			newsArticles.add(createDefaultNewsArticle(articleGroupDTO, 2));
		}
		if (articleGroupDTO.getReadingLevel3() != null && articleGroupDTO.getReadingLevel3()) {
			newsArticles.add(createDefaultNewsArticle(articleGroupDTO, 3));
		}
		if (articleGroupDTO.getReadingLevel4() != null && articleGroupDTO.getReadingLevel4()) {
			newsArticles.add(createDefaultNewsArticle(articleGroupDTO, 4));
		}
		return newsArticles;
	}

	private List<NewsArticleDTO> getNewsArticles(NewsArticleGroupDTO articleGroupDTO) {
		List<NewsArticleDTO> newsArticles = articleGroupDTO.getNewsArticles();
		if (articleGroupDTO.getReadingLevel1() != null && articleGroupDTO.getReadingLevel1()) {
			if (!checkIfNewsArticleExists(newsArticles, 1)) {
				newsArticles.add(createDefaultNewsArticle(articleGroupDTO, 1));
			}
		}
		if (articleGroupDTO.getReadingLevel2() != null && articleGroupDTO.getReadingLevel2()) {
			if (!checkIfNewsArticleExists(newsArticles, 2)) {
				newsArticles.add(createDefaultNewsArticle(articleGroupDTO, 2));
			}
		}
		if (articleGroupDTO.getReadingLevel3() != null && articleGroupDTO.getReadingLevel3()) {
			if (!checkIfNewsArticleExists(newsArticles, 3)) {
				newsArticles.add(createDefaultNewsArticle(articleGroupDTO, 3));
			}
		}
		if (articleGroupDTO.getReadingLevel4() != null && articleGroupDTO.getReadingLevel4()) {
			if (!checkIfNewsArticleExists(newsArticles, 4)) {
				newsArticles.add(createDefaultNewsArticle(articleGroupDTO, 4));
			}
		}
		return newsArticles;
	}

	private NewsArticleDTO createDefaultNewsArticle(NewsArticleGroupDTO articleGroupDTO, int readingLevel) {
		NewsArticleDTO article = new NewsArticleDTO();
		article.setRecordInUse(RecordInUseType.Y);
		article.setOperationDateTime(articleGroupDTO.getOperationDateTime());
		article.setReadingLevel(readingLevel);
		return article;
	}

	private boolean checkIfNewsArticleExists(List<NewsArticleDTO> articles, int readingLevel) {
		boolean found = false;
		for (NewsArticleDTO newsArticleDTO : articles) {
			if (newsArticleDTO.getReadingLevel() == readingLevel) {
				found = true;
				break;
			}
		}
		return found;
	}

	public NewsArticleGroupDTO getArticleGroup(Long articleGroupId) {
		NewsArticleGroup articleGroup = newsArticleGroupService.load(articleGroupId);
		NewsArticleGroupDTO articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		List<NewsArticleDTO> articles = newsArticleHelper.getByArticleGroupId(articleGroupId);
		articleGroupDTO.setNewsArticles(articles);
		return articleGroupDTO;
	}
}
