package com.enewschamp.app.launchapp.page.handler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.savedarticle.service.SavedNewsArticleService;
import com.enewschamp.app.student.business.StudentActivityBusiness;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "LaunchAppPageHandler")
public class LaunchAppPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	private StudentActivityBusiness studentActivityBusiness;
	@Autowired
	private NewsArticleService newsArticleService;

	@Autowired
	SavedNewsArticleService savedNewsArticleService;

	@Autowired
	GenreService genreService;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		// no page number to be returned..
		pageDto.getHeader().setPageNo(0);
		pageDto.getHeader().setPageCount(0);
		pageDto.getHeader().setPageSize(appConfig.getPageSize());
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		return pageDTO;
	}

}
