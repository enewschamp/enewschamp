package com.enewschamp.article.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.article.pagedata.NewsArticleSearchPageData;
import com.enewschamp.domain.common.IPageBuilder;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.user.domin.service.UserService;

@Component(value="NewsArticleSearchPageBuilder")
public class NewsArticleSearchPageBuilder implements IPageBuilder  {

	@Autowired
	GenreService genreService;

	@Autowired
	UserService userService;
	
	@Autowired
	EditionService editionService;
	
	public PageDTO buildPage() {
		
		PageDTO page = new PageDTO();
		NewsArticleSearchPageData data = new NewsArticleSearchPageData();
		
		data.setGenreLOV(genreService.getLOV());
		data.setPublisherLOV(userService.getPublisherLOV());
		data.setAuthorLOV(userService.getAuthorLOV());
		data.setEditorLOV(userService.getEditorLOV());
		data.setMonthsLOV(MonthType.getLOV());
		data.setDaysLOV(WeekDayType.getLOV());
		data.setEditionsLOV(editionService.getLOV());
		page.setData(data);
		page.setPageName("SearchNewsArticles");
		return page;
	}
}
