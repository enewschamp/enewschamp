package com.enewschamp.article.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;

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
	
	public NewsArticleGroup create(NewsArticleGroup articleGroup) {
		return repository.save(articleGroup);
	}
	
	public NewsArticleGroup update(NewsArticleGroup newsArticleGroup) {
		Long articleGroupId = newsArticleGroup.getNewsArticleGroupId();
		NewsArticleGroup existingEntity = get(articleGroupId);
		modelMapper.map(newsArticleGroup, existingEntity);
		return repository.save(existingEntity);
	}
	
	public NewsArticleGroup patch(NewsArticleGroup newsArticleGroup) {
		Long articleGroupId = newsArticleGroup.getNewsArticleGroupId();
		NewsArticleGroup existingEntity = get(articleGroupId);
		modelMapperForPatch.map(newsArticleGroup, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long articleId) {
		repository.deleteById(articleId);
	}
	
	public NewsArticleGroup get(Long articleGroupId) {
		Optional<NewsArticleGroup> existingEntity = repository.findById(articleGroupId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else { 
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.ARTICLE_GROUP_NOT_FOUND, "Article not found!");
		}
	}
	
	public String getAudit(Long articleGroupId) {
		NewsArticleGroup articleGroup = new NewsArticleGroup();
		articleGroup.setNewsArticleGroupId(articleGroupId);
		return auditService.getEntityAudit(articleGroup);
	}

}
