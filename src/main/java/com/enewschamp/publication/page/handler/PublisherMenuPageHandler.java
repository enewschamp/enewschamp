package com.enewschamp.publication.page.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.AbstractPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;

@Component(value="PublisherMenuPageHandler")
public class PublisherMenuPageHandler extends AbstractPageHandler  {

	@Autowired
	private EnewschampApplicationProperties appConfig;
	
	@Override
	public PageDTO handlePageAction(String actionName, PageRequestDTO pageRequest) {
		
		PageDTO page = new PageDTO();
		return page;
	}
	

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String action = pageNavigationContext.getActionName();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionID();
		return pageDto;

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
