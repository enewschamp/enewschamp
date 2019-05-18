package com.enewschamp.article.page.handler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.article.app.service.NewsArticleGroupHelper;
import com.enewschamp.article.app.service.NewsArticleHelper;
import com.enewschamp.article.page.data.NewsArticleGroupPageData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.user.domin.service.UserService;

@Component(value="NewsArticleListPageHandler")
public class NewsArticleListPageHandler implements IPageHandler  {

	
	@Autowired
	private NewsArticleGroupHelper newsArticleGroupHelper;
	
	@Autowired
	private NewsArticleHelper newsArticleHelper;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	GenreService genreService;

	@Autowired
	UserService userService;
	
	@Autowired
	EditionService editionService;
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		
		PageDTO pageDTO = new PageDTO();

		Long articleGroupId = null;
		articleGroupId = pageRequest.getData().get("newsArticleGroupId").asLong();
		
		NewsArticleGroupPageData data = new NewsArticleGroupPageData();
		
		data.setGenreLOV(genreService.getLOV());
		data.setPublisherLOV(userService.getPublisherLOV());
		data.setAuthorLOV(userService.getAuthorLOV());
		data.setEditorLOV(userService.getEditorLOV());
		data.setMonthsLOV(MonthType.getLOV());
		data.setDaysLOV(WeekDayType.getLOV());
		data.setEditionsLOV(editionService.getLOV());
		
		data.setNewsArticleGroup(newsArticleGroupHelper.getArticleGroup(articleGroupId));
		data.setNewsArticles(newsArticleHelper.getByArticleGroupId(articleGroupId));
		
		pageDTO.setData(data);
		return pageDTO;
	}
}
