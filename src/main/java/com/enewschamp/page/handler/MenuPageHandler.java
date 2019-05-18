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
import com.enewschamp.user.domin.service.UserService;

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
		if(actionName.equals("ClickSearchNewsArticle")) {
			NewsArticleSearchPageData data = new NewsArticleSearchPageData();
			
			data.setGenreLOV(genreService.getLOV());
			data.setPublisherLOV(userService.getPublisherLOV());
			data.setAuthorLOV(userService.getAuthorLOV());
			data.setEditorLOV(userService.getEditorLOV());
			data.setMonthsLOV(MonthType.getLOV());
			data.setDaysLOV(WeekDayType.getLOV());
			data.setEditionsLOV(editionService.getLOV());
			page.setData(data);
		}
		return page;
	}
	
	
}
