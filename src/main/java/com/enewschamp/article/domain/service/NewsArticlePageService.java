package com.enewschamp.article.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.article.page.dto.NewsArticleSearchPage;
import com.enewschamp.article.page.dto.NewsArticleSearchPageData;
import com.enewschamp.publication.domain.service.GenreService;

@Service
public class NewsArticlePageService  {

	@Autowired
	GenreService genreService;

	public NewsArticleSearchPage buildNewsArticleSearchPage() {
		
		NewsArticleSearchPage page = new NewsArticleSearchPage();
		
		NewsArticleSearchPageData data = new NewsArticleSearchPageData();
		data.setGenreLOV(genreService.getLOV());
		
		page.setData(data);
		return page;
	}
}
