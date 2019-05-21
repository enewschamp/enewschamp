package com.enewschamp.publication.page.handler;

import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;

@Component(value="PublicationListPageHandler")
public class PublicationListPageHandler implements IPageHandler  {

	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		
		return new PageDTO();
	}
	
	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		return pageNavigationContext.getPreviousPageResponse();
	}
}
