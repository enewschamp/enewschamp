package com.enewschamp.article.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;

public interface NewsArticleRepositoryCustom {
	public Page<NewsArticleDTO> findAllPage(NewsArticleSearchRequest searchRequest, Pageable pageable);
}
