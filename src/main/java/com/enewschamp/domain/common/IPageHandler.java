package com.enewschamp.domain.common;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;

public interface IPageHandler {
	
	// Method to process the data based on action take on the page
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest);
	
}