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

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.domain.common.PageBuilderFactory;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class PageController {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private PageBuilderFactory pageBuilderFactory;

	
	@PostMapping(value = "/pages/{pageName}/{actionName}")
	public ResponseEntity<PageDTO> get(@PathVariable String pageName, @PathVariable String actionName, @RequestBody PageDTO pageData) {
		PageDTO page = pageBuilderFactory.getPageBuilder(pageName, actionName).buildPage();
		addSuccessHeader(page);
		return new ResponseEntity<PageDTO>(page, HttpStatus.OK);
	}
	
	private void addSuccessHeader(PageDTO page) {
		HeaderDTO header = new HeaderDTO();
		page.setHeader(header);
		
		header.setRequestStatus(RequestStatusType.S);
		header.setPageName(page.getPageName());
	}
	
}
