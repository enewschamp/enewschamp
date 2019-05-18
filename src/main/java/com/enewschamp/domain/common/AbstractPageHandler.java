package com.enewschamp.domain.common;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;

public abstract class AbstractPageHandler implements IPageHandler {

	// Method to process the data based on action take on the page
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDTO = handlePageAction(actionName, pageRequest);
		return pageDTO;
	}

	protected abstract PageDTO handlePageAction(String actionName, PageRequestDTO pageRequest);

}