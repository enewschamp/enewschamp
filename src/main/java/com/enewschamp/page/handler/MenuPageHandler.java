package com.enewschamp.page.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.article.page.data.NewsArticleSearchPageData;
import com.enewschamp.domain.common.AbstractPageHandler;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.publication.page.data.PublicationSearchPageData;
import com.enewschamp.user.domain.service.UserService;

@Component(value="MenuPageHandler")
public class MenuPageHandler extends AbstractPageHandler  {

	@Autowired
	GenreService genreService;

	@Autowired
	UserService userService;
	
	@Autowired
	EditionService editionService;
	
	@Override
	public PageDTO handlePageAction(String actionName, PageRequestDTO pageRequest) {
		
		PageDTO page = new PageDTO();
		switch(actionName) {
			case "ClickSearchNewsArticle":
				NewsArticleSearchPageData newsArticleSearchPageData = new NewsArticleSearchPageData();
				newsArticleSearchPageData.setGenreLOV(genreService.getLOV());
				newsArticleSearchPageData.setPublisherLOV(userService.getPublisherLOV());
				newsArticleSearchPageData.setAuthorLOV(userService.getAuthorLOV());
				newsArticleSearchPageData.setEditorLOV(userService.getEditorLOV());
				newsArticleSearchPageData.setMonthsLOV(MonthType.getLOV());
				newsArticleSearchPageData.setDaysLOV(WeekDayType.getLOV());
				newsArticleSearchPageData.setEditionsLOV(editionService.getLOV());
				page.setData(newsArticleSearchPageData);
			break;
			
			case "ClickSearchPublications":
				PublicationSearchPageData publicationSearchPageData = new PublicationSearchPageData();
				publicationSearchPageData.setPublisherLOV(userService.getPublisherLOV());
				publicationSearchPageData.setAuthorLOV(userService.getAuthorLOV());
				publicationSearchPageData.setEditorLOV(userService.getEditorLOV());
				publicationSearchPageData.setEditionsLOV(editionService.getLOV());
				page.setData(publicationSearchPageData);
			break;
		}
		return page;
	}
	
	
}
