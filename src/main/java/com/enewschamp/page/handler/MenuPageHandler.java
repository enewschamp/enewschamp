package com.enewschamp.page.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.domain.common.AbstractPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.user.domain.service.UserService;

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
}
