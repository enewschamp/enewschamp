package com.enewschamp.app.school.repository;

import java.util.List;

import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;

public interface NewsArticleSummaryRepository {

	public List<NewsArticleSummaryDTO> getArticleDetails(Long newsArticleId, Long studentId);

}
