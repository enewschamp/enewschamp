package com.enewschamp.article.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.article.page.dto.NewsArticleSearchPage;
import com.enewschamp.article.page.dto.NewsArticleSearchPageData;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.user.domin.service.UserService;

@Service
public class NewsArticlePageService  {

	@Autowired
	GenreService genreService;

	@Autowired
	UserService userService;

	
	public NewsArticleSearchPage buildNewsArticleSearchPage() {
		
		NewsArticleSearchPage page = new NewsArticleSearchPage();
		
		NewsArticleSearchPageData data = new NewsArticleSearchPageData();
		
		data.setGenreLOV(genreService.getLOV());
		data.setPublisherLOV(userService.getPublisherLOV());
		data.setAuthorLOV(userService.getAuthorLOV());
		data.setEditorLOV(userService.getEditorLOV());
		data.setMonthsLOV(MonthType.getLOV());
		data.setDaysLOV(WeekDayType.getLOV());
		
		page.setData(data);
		return page;
	}
}
