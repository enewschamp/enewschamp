package com.enewschamp.article.app.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.app.dto.NewsArticleQuizDTO;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.article.domain.service.NewsArticleQuizRepository;
import com.enewschamp.article.domain.service.NewsArticleQuizService;
import com.enewschamp.article.domain.service.NewsArticleRepository;
import com.enewschamp.article.domain.service.NewsArticleService;

import lombok.extern.java.Log;

@Log
@Component
public class NewsArticleHelper {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private NewsArticleService newsArticleService;
	
	@Autowired
	private NewsArticleRepository newsArticleRepository;
	
	@Autowired
	private NewsArticleQuizService newsArticleQuizService;
	
	@Autowired
	private NewsArticleQuizRepository newsArticleQuizRepository;

	public NewsArticleDTO create(NewsArticleDTO articleDTO) {
		NewsArticle article = modelMapper.map(articleDTO, NewsArticle.class);
		article = newsArticleService.create(article);
		articleDTO.setNewsArticleId(article.getNewsArticleId());
		
		List<NewsArticleQuizDTO> quizSet = new ArrayList<NewsArticleQuizDTO>(); 
		for(NewsArticleQuizDTO quizDTO: articleDTO.getNewsArticleQuiz()) {
			quizDTO.setNewsArticleId(article.getNewsArticleId());
			NewsArticleQuiz articleQuiz = modelMapper.map(quizDTO, NewsArticleQuiz.class);
			articleQuiz.setRecordInUse(article.getRecordInUse());
			articleQuiz.setOperatorId(article.getOperatorId());
			articleQuiz = newsArticleQuizService.create(articleQuiz);
			quizDTO.setNewsArticleQuizId(articleQuiz.getNewsArticleQuizId());
			
			quizSet.add(quizDTO);
		}
		articleDTO.setNewsArticleQuiz(quizSet);
		
		return articleDTO;
	}

	public NewsArticleDTO get(Long articleId) {
		NewsArticle article = newsArticleService.get(articleId);
		NewsArticleDTO articleDTO = modelMapper.map(article, NewsArticleDTO.class);
		newsArticleService.get(articleId);
		return articleDTO;
	}
	
	public List<NewsArticleDTO> getByArticleGroupId(long articleGroupId) {
		List<NewsArticle> articles = newsArticleRepository.findByNewsArticleGroupId(articleGroupId);
		List<NewsArticleDTO> articleDTOs = new ArrayList<NewsArticleDTO>();
		for(NewsArticle article: articles) {
			NewsArticleDTO articleDTO = modelMapper.map(article, NewsArticleDTO.class);
			articleDTO = fillQuiz(articleDTO);
			articleDTOs.add(articleDTO);
		}
		return articleDTOs;
	}
	
	private NewsArticleDTO fillQuiz(NewsArticleDTO articleDTO) {
		List<NewsArticleQuiz> quizList = newsArticleQuizRepository.findByNewsArticleId(articleDTO.getNewsArticleId());
		List<NewsArticleQuizDTO> quizDTOs = new ArrayList<NewsArticleQuizDTO>();
		for(NewsArticleQuiz quiz: quizList) {
			NewsArticleQuizDTO quizDTO = modelMapper.map(quiz, NewsArticleQuizDTO.class);
			quizDTOs.add(quizDTO);
		}
		articleDTO.setNewsArticleQuiz(quizDTOs);
		return articleDTO;
	}
	

}
