package com.enewschamp.article.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.app.dto.PublisherNewsArticleSummaryDTO;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;

public interface NewsArticleRepositoryCustom {

	public Page<NewsArticleSummaryDTO> findArticles(NewsArticleSearchRequest searchRequest, Long studentId,
			Pageable pageable);

	public Page<PublisherNewsArticleSummaryDTO> findPublisherArticles(NewsArticleSearchRequest searchRequest,
			Pageable pageable);
}
