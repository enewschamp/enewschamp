package com.enewschamp.article.page.handler;

import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;

@Component(value="NewsArticleListPageHandler")
public class NewsArticleListPageHandler implements IPageHandler  {
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		
		return new PageDTO();
	}
	
	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		return pageNavigationContext.getPreviousPageResponse();
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		// TODO Auto-generated method stub
		return null;
	}
}