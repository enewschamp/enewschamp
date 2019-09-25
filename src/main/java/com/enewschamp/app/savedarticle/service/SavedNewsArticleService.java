package com.enewschamp.app.savedarticle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSearchRequest;
import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSummaryDTO;
import com.enewschamp.app.savedarticle.repository.SavedNewsArticleCustomRepositoryImpl;

@Service
public class SavedNewsArticleService {

	@Autowired
	private SavedNewsArticleCustomRepositoryImpl savedNewsArticleCustomRepository;
	
	
	public Page<SavedNewsArticleSummaryDTO> findSavedArticles(SavedNewsArticleSearchRequest searchRequest, HeaderDTO header) {
		int pageNumber = header.getPageNo();
		pageNumber = pageNumber > 0 ? (pageNumber - 1) : 0;
		Pageable pageable = PageRequest.of(pageNumber, header.getPageSize());
		return savedNewsArticleCustomRepository.findArticles(searchRequest, pageable);
	}
	
}
