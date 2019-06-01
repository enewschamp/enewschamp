package com.enewschamp.page.handler;

import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;

import com.enewschamp.domain.common.AbstractPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;

@Component(value="MenuPageHandler")
public class MenuPageHandler extends AbstractPageHandler  {

	@Override
	public PageDTO handlePageAction(String actionName, PageRequestDTO pageRequest) {
		
		PageDTO page = new PageDTO();
		return page;
	}
	

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		throw new UnsupportedOperationException();
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
