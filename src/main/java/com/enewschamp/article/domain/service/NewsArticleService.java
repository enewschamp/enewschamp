package com.enewschamp.article.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.audit.domain.AuditBuilder;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
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
	
	public NewsArticle create(NewsArticle article) {
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
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.ARTICLE_NOT_FOUND, "Article not found!");
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
	
}
