package com.enewschamp.article.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
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

}
