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
		
		//Process current page
		PageDTO responsePage = pageHandlerFactory.getPageHandler(pageName).handleAction(actionName, pageRequest);
		addSuccessHeader(responsePage);
		
		String nextPageName = appConfig.getPageNavigationConfig().get(pageName.toLowerCase()).get(actionName.toLowerCase());
		responsePage.setPageName(nextPageName);
		return new ResponseEntity<PageDTO>(responsePage, HttpStatus.OK);
	}
	
	private void addSuccessHeader(PageDTO page) {
		HeaderDTO header = new HeaderDTO();
		page.setHeader(header);
		
		header.setRequestStatus(RequestStatusType.S);
		header.setPageName(page.getPageName());
	}
	
}
