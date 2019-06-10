package com.enewschamp.article.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.article.domain.common.ArticleGroupStatusType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;

@Service
public class NewsArticleGroupService {

	@Autowired
	NewsArticleGroupRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	@Autowired 
	private NewsArticleService newsArticleService;
	
	public NewsArticleGroup create(NewsArticleGroup articleGroup) {
		deriveStatus(articleGroup);
		return repository.save(articleGroup);
	}
	
	public NewsArticleGroup update(NewsArticleGroup newsArticleGroup) {
		Long articleGroupId = newsArticleGroup.getNewsArticleGroupId();
		NewsArticleGroup existingEntity = load(articleGroupId);
		modelMapper.map(newsArticleGroup, existingEntity);
		return repository.save(existingEntity);
	}
	
	public NewsArticleGroup patch(NewsArticleGroup newsArticleGroup) {
		Long articleGroupId = newsArticleGroup.getNewsArticleGroupId();
		NewsArticleGroup existingEntity = load(articleGroupId);
		modelMapperForPatch.map(newsArticleGroup, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long articleId) {
		repository.deleteById(articleId);
	}
	
	public NewsArticleGroup load(Long articleGroupId) {
		Optional<NewsArticleGroup> existingEntity = repository.findById(articleGroupId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else { 
			throw new BusinessException(ErrorCodes.ARTICLE_GROUP_NOT_FOUND);
		}
	}
	
	public NewsArticleGroup get(Long articleGroupId) {
		if(articleGroupId == null) {
			return null;
		}
		Optional<NewsArticleGroup> existingEntity = repository.findById(articleGroupId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}
	
	public String getAudit(Long articleGroupId) {
		NewsArticleGroup articleGroup = new NewsArticleGroup();
		articleGroup.setNewsArticleGroupId(articleGroupId);
		return auditService.getEntityAudit(articleGroup);
	}
	
	public ArticleGroupStatusType deriveStatus(NewsArticleGroup articleGroup) {
		ArticleGroupStatusType newStatus = deriveStatus(articleGroup.getNewsArticles());
		articleGroup.setStatus(newStatus);
		return newStatus;
	}
	
	private ArticleGroupStatusType deriveStatus(List<NewsArticle> articles) {
		ArticleGroupStatusType newStatus = null;
		if(articles != null) {
			for(NewsArticle article: articles) {
				ArticleStatusType articleStatus = newsArticleService.deriveArticleStatus(article);
				if(articleStatus != null) {
					ArticleGroupStatusType status = ArticleGroupStatusType.fromArticleStatus(articleStatus);
					if(status.equals(ArticleGroupStatusType.WIP)) {
						newStatus = status;
						break;
					}
					if(newStatus == null) {
						newStatus = status;
						continue;
					}
					if(status.getOrder() < newStatus.getOrder()) {
						newStatus = status;
					}
				}
			}
		}
		return newStatus;
	}
	
	public NewsArticleGroup assignAuthor(Long articleGroupId, String authorId) {
		NewsArticleGroup articleGroup = load(articleGroupId);
		articleGroup.setAuthorId(authorId);
		List<NewsArticle> articles = newsArticleService.assignAuthor(articleGroupId, authorId);
		articleGroup.setStatus(deriveStatus(articles));
		repository.save(articleGroup);
		return articleGroup;
	}
	
	public NewsArticleGroup assignEditor(Long articleGroupId, String editorId) {
		NewsArticleGroup articleGroup = load(articleGroupId);
		articleGroup.setEditorId(editorId);
		List<NewsArticle> articles = newsArticleService.assignEditor(articleGroupId, editorId);
		repository.save(articleGroup);
		return articleGroup;
	}
}
