package com.enewschamp.app.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.enewschamp.domain.common.PageNavigationContext;

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
	
//	@PostMapping(value = "/pages/{pageName}/{actionName}")
//	public ResponseEntity<PageDTO> get(@PathVariable String pageName, @PathVariable String actionName, @RequestBody PageRequestDTO pageRequest) {
//		
//		pageRequest.getHeader().setPageName(pageName);
//		pageRequest.getHeader().setAction(actionName);
//		
//		PageDTO pageResponse = processRequest(pageName, actionName, pageRequest);
//		
//		return new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
//	}
	
	@PostMapping(value = "/app")
	public ResponseEntity<PageDTO> processAppRequest(@RequestBody PageRequestDTO pageRequest) {
		
		String pageName = pageRequest.getHeader().getPageName();
		String actionName = pageRequest.getHeader().getAction();
		pageRequest.getHeader().setPageName(pageName);
		pageRequest.getHeader().setAction(actionName);
		
		PageDTO pageResponse = processRequest(pageName, actionName, pageRequest, "app");
		
		return new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
	}
	
	@PostMapping(value = "/publisher")
	@Transactional
	public ResponseEntity<PageDTO> processPublisherAppRequest(@RequestBody PageRequestDTO pageRequest) {
		
		String pageName = pageRequest.getHeader().getPageName();
		String actionName = pageRequest.getHeader().getAction();
		pageRequest.getHeader().setPageName(pageName);
		pageRequest.getHeader().setAction(actionName);
		
		PageDTO pageResponse = processRequest(pageName, actionName, pageRequest, "publisher");
		
		return new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
	}

	private PageDTO processRequest(String pageName, String actionName, PageRequestDTO pageRequest, String context) {
		//Process current page
		PageDTO pageResponse = pageHandlerFactory.getPageHandler(pageName, context).handleAction(actionName, pageRequest);
		
		//Load next page
		String nextPageName = appConfig.getPageNavigationConfig().get(context).get(pageName.toLowerCase()).get(actionName.toLowerCase());
		if(!pageName.equals(nextPageName) && !nextPageName.isEmpty()) {
			PageNavigationContext pageNavigationContext = new PageNavigationContext();
			pageNavigationContext.setActionName(actionName);
			pageNavigationContext.setPageRequest(pageRequest);
			pageNavigationContext.setPreviousPageResponse(pageResponse);
			pageResponse = pageHandlerFactory.getPageHandler(nextPageName, context).loadPage(pageNavigationContext);
		}
		
		addSuccessHeader(pageName, actionName, pageResponse, context);
		return pageResponse;
	}
	
	private void addSuccessHeader(String currentPageName, String actionName, PageDTO page, String context) {
		if(page.getHeader() == null) {
			page.setHeader(new HeaderDTO());
		}
		page.getHeader().setRequestStatus(RequestStatusType.S);
		page.getHeader().setPageName(page.getPageName());
		
		String nextPageName = appConfig.getPageNavigationConfig().get(context).get(currentPageName.toLowerCase()).get(actionName.toLowerCase());
		page.getHeader().setPageName(nextPageName);
	}
	
}