package com.enewschamp.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.domain.common.PageHandlerFactory;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class PageController {

	@Autowired
	private PageHandlerFactory pageHandlerFactory;
	
	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	ModelMapper modelMapper;
	
	@PostMapping(value = "/pages/{pageName}/{actionName}")
	public ResponseEntity<PageDTO> get(@PathVariable String pageName, @PathVariable String actionName, @RequestBody PageRequestDTO pageRequest) {
		
		pageRequest.getHeader().setPageName(pageName);
		pageRequest.getHeader().setAction(actionName);
		
		//Process current page
		PageDTO responsePage = pageHandlerFactory.getPageHandler(pageName).handleAction(actionName, pageRequest);
		addSuccessHeader(pageName, actionName, responsePage);
		
		return new ResponseEntity<PageDTO>(responsePage, HttpStatus.OK);
	}
	
	private void addSuccessHeader(String currentPageName, String actionName, PageDTO page) {
		if(page.getHeader() == null) {
			page.setHeader(new HeaderDTO());
		}
		page.getHeader().setRequestStatus(RequestStatusType.S);
		page.getHeader().setPageName(page.getPageName());
		
		String nextPageName = appConfig.getPageNavigationConfig().get(currentPageName.toLowerCase()).get(actionName.toLowerCase());
		page.getHeader().setPageName(nextPageName);
	}
	
}
