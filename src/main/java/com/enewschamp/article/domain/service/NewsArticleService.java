package com.enewschamp.article.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.common.ArticleActionType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.audit.domain.AuditBuilder;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.StatusTransitionDTO;
import com.enewschamp.domain.common.StatusTransitionHandler;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.entity.PublicationArticleLinkage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NewsArticleService {

	@Autowired
	private NewsArticleRepository repository;
	
	@Autowired
	private NewsArticleRepositoryCustom customRepository;
	
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
		deriveArticleStatus(article, true);
		return repository.save(article);
	}
	
	public NewsArticle update(NewsArticle article) {
		deriveArticleStatus(article, true);
		Long articleId = article.getNewsArticleId();
		NewsArticle existingEntity = load(articleId);
		modelMapper.map(article, existingEntity);
		return repository.save(existingEntity);
	}
	
	public NewsArticle patch(NewsArticle article) {
		deriveArticleStatus(article, true);
		Long articleId = article.getNewsArticleId();
		NewsArticle existingEntity = load(articleId);
		modelMapperForPatch.map(article, existingEntity);
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
			throw new BusinessException(ErrorCodes.ARTICLE_NOT_FOUND, String.valueOf(articleId));
		}
	}
	
	public NewsArticle get(Long articleId) {
		if(articleId == null) {
			return null;
		}
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
	
	public ArticleStatusType deriveArticleStatus(NewsArticle article, boolean validateAccess) {
		ArticleStatusType status = ArticleStatusType.Unassigned;
		StatusTransitionDTO transition = null;
		ArticleActionType currentAction = article.getCurrentAction();
		ArticleStatusType existingStatus = null;
		
		NewsArticle existingArticle = get(article.getNewsArticleId());
		
		if(existingArticle == null) { // Check for first time creation
			currentAction = ArticleActionType.SaveNewsArticleGroup;
			existingStatus = ArticleStatusType.Initial;
		} else {
			existingStatus = existingArticle.getStatus();
		}
		if(currentAction != null) {
			transition = new StatusTransitionDTO(NewsArticle.class.getSimpleName(), 
																	 String.valueOf(article.getNewsArticleId()),
																	 existingStatus.toString(),
																	 currentAction.toString(), 
																     null);
			
			String nextStatus = statusTransitionHandler.findNextStatus(transition);
			if(nextStatus.equals(StatusTransitionDTO.REVERSE_STATE)) {
				ArticleStatusType previousStatus = repository.getCurrentStatus(article.getNewsArticleId());
				nextStatus = previousStatus.toString();
			}
			
			status = ArticleStatusType.fromValue(nextStatus);
		} 
		if(transition != null && validateAccess) {
			statusTransitionHandler.validateStateTransitionAccess(transition, article.getAuthorId(), article.getEditorId(), article.getPublisherId(), article.getOperatorId());
			validateStateTransition(article, transition, status);
		}
		article.setStatus(status);
		return status;
	}
	
	private void validateStateTransition(NewsArticle article, StatusTransitionDTO transition, ArticleStatusType newStatus) {
		if(newStatus == null) {
			return;
		}
		switch(newStatus) {
			case ReadyToPublish:
				if(article.getRating() == null) {
					new BusinessException(ErrorCodes.RATING_REQD_FOR_PUBLISH);
				}
			break;
			case ReworkForAuthor:
			case ReworkForEditor:
				if(article.getCurrentComments() == null || article.getCurrentComments().isEmpty()) {
					new BusinessException(ErrorCodes.REWORK_COMMENTS_REQUIRED);
				}
			break;
		}
	}
	
	public List<NewsArticle> assignAuthor(Long articleGroupId, String authorId) {
		
		List<NewsArticle> existingArticles = repository.findByNewsArticleGroupId(articleGroupId);
		for(NewsArticle article: existingArticles) {
			article.setAuthorId(authorId);
			article.setCurrentAction(ArticleActionType.AssignAuthor);
			deriveArticleStatus(article, true);
			repository.save(article);
		}
		return existingArticles;
	}
	
	public List<NewsArticle> assignEditor(Long articleGroupId, String editorId) {
		
		List<NewsArticle> existingArticles = repository.findByNewsArticleGroupId(articleGroupId);
		for(NewsArticle article: existingArticles) {
			article.setEditorId(editorId);
			repository.save(article);
		}
		return existingArticles;
	}
	
	public Page<NewsArticleSummaryDTO> findArticles(NewsArticleSearchRequest searchRequest, HeaderDTO header) {
		int pageNumber = header.getPageNo() != null ? header.getPageNo() : 0;
		pageNumber = pageNumber > 0 ? (pageNumber - 1) : 0;
		Pageable pageable = PageRequest.of(pageNumber, header.getPageSize());
		return customRepository.findArticles(searchRequest, pageable);
	}
	
	
	
	public void markArticlesAsPublished(Publication publication) {
		if(publication == null || publication.getArticleLinkages() == null) {
			return;
		}
		List<PublicationArticleLinkage> articleLinkages = publication.getArticleLinkages(); 
		articleLinkages.forEach(articleLinkage -> {
			NewsArticle article = load(articleLinkage.getNewsArticleId());
			article.setCurrentAction(ArticleActionType.Publish);
			article.setPublishDate(publication.getPublishDate());
			article.setPublisherId(publication.getPublisherId());
			article.setOperatorId(publication.getOperatorId());
			deriveArticleStatus(article, true);
			repository.save(article);
		});
	}
}
