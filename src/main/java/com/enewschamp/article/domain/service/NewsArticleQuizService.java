package com.enewschamp.article.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;

@Service
public class NewsArticleQuizService {

	@Autowired
	NewsArticleQuizRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	public NewsArticleQuiz create(NewsArticleQuiz newsArticleQuiz) {
		return repository.save(newsArticleQuiz);
	}
	
	public NewsArticleQuiz update(NewsArticleQuiz newsArticleQuiz) {
		Long newsArticleQuizId = newsArticleQuiz.getNewsArticleQuizId();
		NewsArticleQuiz existingNewsArticleQuiz = get(newsArticleQuizId);
		modelMapper.map(newsArticleQuiz, existingNewsArticleQuiz);
		return repository.save(existingNewsArticleQuiz);
	}
	
	public NewsArticleQuiz patch(NewsArticleQuiz newsArticleQuiz) {
		Long newsArticleQuizId = newsArticleQuiz.getNewsArticleQuizId();
		NewsArticleQuiz existingEntity = get(newsArticleQuizId);
		modelMapperForPatch.map(newsArticleQuiz, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long newsArticleQuizId) {
		repository.deleteById(newsArticleQuizId);
	}
	
	public NewsArticleQuiz get(Long newsArticleQuizId) {
		Optional<NewsArticleQuiz> existingEntity = repository.findById(newsArticleQuizId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.EDITION_NOT_FOUND);
		}
	}
	
	public String getAudit(Long newsArticleQuizId) {
		NewsArticleQuiz newsArticleQuiz = new NewsArticleQuiz();
		newsArticleQuiz.setNewsArticleQuizId(newsArticleQuizId);
		return auditService.getEntityAudit(newsArticleQuiz);
	}
	
}
