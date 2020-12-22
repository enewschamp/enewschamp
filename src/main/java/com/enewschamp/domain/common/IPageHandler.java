package com.enewschamp.domain.common;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;

public interface IPageHandler {

	// Method to process the data based on action take on the page
	public PageDTO handleAction(PageRequestDTO pageRequest);

	public PageDTO loadPage(PageNavigationContext pageNavigationContext);

	// save to master
	public PageDTO saveAsMaster(PageRequestDTO pageRequest);

	// method to handle actions from app
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO);

}