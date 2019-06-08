package com.enewschamp.article.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.article.domain.common.ArticleActionType;
import com.enewschamp.article.domain.common.ArticleRatingType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.audit.domain.AuditBuilder;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.StatusTransitionDTO;
import com.enewschamp.domain.common.StatusTransitionHandler;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NewsArticleService {

	@Autowired
	private NewsArticleRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private EnewschampApplicationProperties appConfig;
	
	@Autowired
	private StatusTransitionHandler statusTransitionHandler;
	
	public NewsArticle create(NewsArticle article) {
		new ArticleBusinessPolicy(article).validateAndThrow();
		deriveArticleStatus(article);
		return repository.save(article);
	}
	
	public NewsArticle update(NewsArticle article) {
		Long articleId = article.getNewsArticleId();
		NewsArticle existingEntity = load(articleId);
		modelMapper.map(article, existingEntity);
		return repository.save(existingEntity);
	}
	
	public NewsArticle patch(NewsArticle newsArticle) {
		Long articleId = newsArticle.getNewsArticleId();
		NewsArticle existingEntity = load(articleId);
		modelMapperForPatch.map(newsArticle, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long articleId) {
		repository.deleteById(articleId);
	}
	
	public NewsArticle load(Long articleId) {
		Optional<NewsArticle> existingEntity = repository.findById(articleId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.ARTICLE_NOT_FOUND);
		}
	}
	
	public NewsArticle get(Long articleId) {
		Optional<NewsArticle> existingEntity = repository.findById(articleId);
		if(existingEntity.isPresent()) {
			return existingEntity.get();
		}
		return null;
	}
	
	public String getAudit(Long articleId) {
		NewsArticle article = new NewsArticle();
		article.setNewsArticleId(articleId);
		
		AuditBuilder auditBuilder = AuditBuilder.getInstance(auditService, objectMapper, appConfig).forParentObject(article);
		
		// Fetch article quiz changes
		article = load(articleId);
		for(NewsArticleQuiz quiz: article.getNewsArticleQuiz()) {
			auditBuilder.forChildObject(quiz);
		}
		return auditBuilder.build();
	}
	
	public ArticleStatusType deriveArticleStatus(NewsArticle article) {
		ArticleStatusType status = ArticleStatusType.Unassigned;
		if(article.getCurrentAction() != null) {
			ArticleStatusType existingStatus = repository.getArticleStatusType(article.getNewsArticleId());
			existingStatus = existingStatus == null ? ArticleStatusType.Unassigned : existingStatus;
			StatusTransitionDTO transition = new StatusTransitionDTO(NewsArticle.class.getSimpleName(), 
																	 String.valueOf(article.getNewsArticleId()),
																	 existingStatus.toString(),
																     article.getCurrentAction().toString(), 
																     null);
			status = ArticleStatusType.fromValue(statusTransitionHandler.findNextStatus(transition));
		}
		article.setStatus(status);
		return status;
	}
	
	public List<NewsArticle> assignAuthor(Long articleGroupId, String authorId) {
		
		List<NewsArticle> existingArticles = repository.findByNewsArticleGroupId(articleGroupId);
		for(NewsArticle article: existingArticles) {
			article.setAuthorId(authorId);
			article.setCurrentAction(ArticleActionType.AssignAuthor);
			deriveArticleStatus(article);
			repository.save(article);
		}
		return existingArticles;
	}
}
