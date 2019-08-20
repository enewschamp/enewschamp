package com.enewschamp.app.savedarticle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSearchRequest;
import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSummaryDTO;

public interface SavedNewsArticleCustomRepository{

	
	public Page<SavedNewsArticleSummaryDTO> findArticles(SavedNewsArticleSearchRequest searchRequest, Pageable pageable);

}
