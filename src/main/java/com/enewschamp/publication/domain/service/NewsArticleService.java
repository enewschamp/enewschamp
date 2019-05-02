package com.enewschamp.publication.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.publication.domain.common.NewsArticleErrorCodes;
import com.enewschamp.publication.domain.entity.NewsArticle;

@Service
public class NewsArticleService {

	@Autowired
	NewsArticleRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	public NewsArticle create(NewsArticle article) {
		return repository.save(article);
	}
	
	public NewsArticle update(NewsArticle article) {
		Long articleId = article.getNewsArticleId();
		NewsArticle existingEntity = get(articleId);
		modelMapper.map(article, existingEntity);
		return repository.save(existingEntity);
	}
	
	public NewsArticle patch(NewsArticle newsArticle) {
		Long articleId = newsArticle.getNewsArticleId();
		NewsArticle existingEntity = get(articleId);
		modelMapperForPatch.map(newsArticle, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long articleId) {
		repository.deleteById(articleId);
	}
	
	public NewsArticle get(Long articleId) {
		Optional<NewsArticle> existingEntity = repository.findById(articleId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), NewsArticleErrorCodes.ARTICLE_NOT_FOUND, "Article not found!");
		}
	}
	
	public String getAudit(Long articleId) {
		NewsArticle article = new NewsArticle();
		article.setNewsArticleId(articleId);
		return auditService.getEntityAudit(article);
	}
	
}
