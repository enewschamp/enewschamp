package com.enewschamp.article.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.app.dto.NewsArticleQuizDTO;
import com.enewschamp.article.domain.common.ArticleGroupStatusType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.article.domain.service.NewsArticleGroupRepository;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleQuizRepository;
import com.enewschamp.article.domain.service.NewsArticleRepository;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.publication.domain.common.PublicationStatusType;

import lombok.extern.java.Log;

@Log
@Component
public class NewsArticleHelper {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private NewsArticleService newsArticleService;

	@Autowired
	private NewsArticleGroupService newsArticleGroupService;

	@Autowired
	private NewsArticleRepository newsArticleRepository;

	@Autowired
	NewsArticleGroupRepository repository;

	@Autowired
	private NewsArticleQuizRepository newsArticleQuizRepository;

	public NewsArticleDTO create(NewsArticleDTO articleDTO) {
		List<NewsArticleQuizDTO> newsArticleQuiz = articleDTO.getNewsArticleQuiz();
		// Remove questions which have been deleted on the UI
		NewsArticle existingArticle = newsArticleService.get(articleDTO.getNewsArticleId());
		if (existingArticle != null) {
			removeDelinkedQuestions(articleDTO.getNewsArticleId(), newsArticleQuiz);
		}
		if (newsArticleQuiz != null) {
			for (NewsArticleQuizDTO quizDTO : newsArticleQuiz) {
				quizDTO.setOperatorId(articleDTO.getOperatorId());
				quizDTO.setRecordInUse(articleDTO.getRecordInUse());
			}
		}

		NewsArticle article = modelMapper.map(articleDTO, NewsArticle.class);
		article = newsArticleService.create(article);
		articleDTO = modelMapper.map(article, NewsArticleDTO.class);

		newsArticleQuiz = articleDTO.getNewsArticleQuiz();
		if (newsArticleQuiz != null) {
			for (NewsArticleQuizDTO quizDTO : newsArticleQuiz) {
				quizDTO.setNewsArticleId(articleDTO.getNewsArticleId());
			}
		}
		return articleDTO;
	}

	private void removeDelinkedQuestions(long articleId, List<NewsArticleQuizDTO> questions) {
		if (articleId <= 0) {
			return;
		}
		NewsArticle article = newsArticleService.get(articleId);
		if (article == null) {
			return;
		}

		for (NewsArticleQuiz question : article.getNewsArticleQuiz()) {
			if (!isExistingLinkageFound(question.getNewsArticleQuizId(), questions)) {
				newsArticleQuizRepository.deleteById(question.getNewsArticleQuizId());
			}
		}
	}

	private boolean isExistingLinkageFound(long questionId, List<NewsArticleQuizDTO> existingQuestions) {
		for (NewsArticleQuizDTO question : existingQuestions) {
			if (question.getNewsArticleQuizId() == questionId) {
				return true;
			}
		}
		return false;
	}

	public NewsArticleDTO get(Long articleId) {
		NewsArticle article = newsArticleService.get(articleId);
		NewsArticleDTO articleDTO = modelMapper.map(article, NewsArticleDTO.class);
		articleDTO = fillQuiz(articleDTO);
		return articleDTO;
	}

	public NewsArticle updatePublicationId(NewsArticleDTO newsArticleLinkageDTO,
			PublicationStatusType publicationStatus, LocalDate publicationDate) {
		NewsArticle article = newsArticleService.get(newsArticleLinkageDTO.getNewsArticleId());
		article.setPublicationId(newsArticleLinkageDTO.getPublicationId());
		article.setPublicationDate(publicationDate);
		article.setSequence(newsArticleLinkageDTO.getSequence());
		if (PublicationStatusType.Published.equals(publicationStatus)) {
			article.setStatus(ArticleStatusType.Published, article.getStatus());
			NewsArticleGroup articleGroup = newsArticleGroupService.get(article.getNewsArticleGroupId());
			List<NewsArticle> articles = newsArticleRepository
					.findByNewsArticleGroupId(articleGroup.getNewsArticleGroupId());
			ArticleGroupStatusType newStatus = newsArticleGroupService.deriveNewStatus(articles);
			if (ArticleGroupStatusType.Published.equals(newStatus)) {
				articleGroup.setStatus(newStatus);
				articleGroup.setNewsArticles(articles);
				articleGroup = repository.save(articleGroup);
			}
		}
		article = newsArticleRepository.save(article);
		return article;
	}

	public List<NewsArticleDTO> getByArticleGroupId(long articleGroupId) {
		List<NewsArticle> articles = newsArticleRepository.findByNewsArticleGroupId(articleGroupId);
		List<NewsArticleDTO> articleDTOs = new ArrayList<NewsArticleDTO>();
		for (NewsArticle article : articles) {
			NewsArticleDTO articleDTO = modelMapper.map(article, NewsArticleDTO.class);
			articleDTO = fillQuiz(articleDTO);
			articleDTOs.add(articleDTO);
		}
		return articleDTOs;
	}

	public List<NewsArticleDTO> getByPublicationId(long publicationId) {
		List<NewsArticle> articles = newsArticleRepository.findByPublicationId(publicationId);
		List<NewsArticleDTO> articleDTOs = new ArrayList<NewsArticleDTO>();
		for (NewsArticle article : articles) {
			NewsArticleDTO articleDTO = modelMapper.map(article, NewsArticleDTO.class);
			articleDTO = fillQuiz(articleDTO);
			articleDTOs.add(articleDTO);
		}
		return articleDTOs;
	}

	private NewsArticleDTO fillQuiz(NewsArticleDTO articleDTO) {
		List<NewsArticleQuiz> quizList = newsArticleQuizRepository.findByNewsArticleId(articleDTO.getNewsArticleId());
		List<NewsArticleQuizDTO> quizDTOs = new ArrayList<NewsArticleQuizDTO>();
		for (NewsArticleQuiz quiz : quizList) {
			NewsArticleQuizDTO quizDTO = modelMapper.map(quiz, NewsArticleQuizDTO.class);
			quizDTOs.add(quizDTO);
		}
		articleDTO.setNewsArticleQuiz(quizDTOs);
		return articleDTO;
	}

	public void deleteByArticleGroupId(long articleGroupId) {
		newsArticleRepository.deleteByArticleGroupId(articleGroupId);
	}
}