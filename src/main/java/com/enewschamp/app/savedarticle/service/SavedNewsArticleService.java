package com.enewschamp.app.savedarticle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSearchRequest;
import com.enewschamp.app.savedarticle.repository.SavedNewsArticleCustomRepositoryImpl;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.opinions.page.dto.OpinionsSearchRequest;

@Service
public class SavedNewsArticleService {

	@Autowired
	private SavedNewsArticleCustomRepositoryImpl savedNewsArticleCustomRepository;

	public Page<NewsArticleSummaryDTO> findSavedArticles(SavedNewsArticleSearchRequest searchRequest, int pageNo,
			int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		return savedNewsArticleCustomRepository.findArticles(searchRequest, pageable);
	}

	public Page<NewsArticleSummaryDTO> findArticlesWithOpinions(OpinionsSearchRequest searchRequest, int pageNo,
			int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		return savedNewsArticleCustomRepository.findArticlesWithOpinions(searchRequest, pageable);
	}

}
