package com.enewschamp.app.savedarticle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSearchRequest;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.opinions.page.dto.OpinionsSearchRequest;

public interface SavedNewsArticleCustomRepository {

	public Page<NewsArticleSummaryDTO> findArticles(SavedNewsArticleSearchRequest searchRequest, Pageable pageable);

	public Page<NewsArticleSummaryDTO> findArticlesWithOpinions(OpinionsSearchRequest searchRequest, Pageable pageable);

}
