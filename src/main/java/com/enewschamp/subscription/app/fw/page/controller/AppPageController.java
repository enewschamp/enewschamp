package com.enewschamp.subscription.app.fw.page.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.enewschamp.app.common.uicontrols.service.UIControlsService;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.domain.common.PageHandlerFactory;
import com.enewschamp.domain.common.PageNavigationContext;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp/ui/v1")
public class AppPageController {


	@Autowired
	private PageHandlerFactory pageHandlerFactory;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	UIControlsService uiControlService;
	
	@Autowired
	PageNavigationService pageNavigationService;

	
	@PostMapping(value = "/app")
	public ResponseEntity<PageDTO> processAppRequest(@RequestBody PageRequestDTO pageRequest) {
		
		String pageName = pageRequest.getHeader().getPageName();
		String actionName = pageRequest.getHeader().getAction();
		pageRequest.getHeader().setPageName(pageName);
		pageRequest.getHeader().setAction(actionName);
		
		PageDTO pageResponse = processRequest(pageName, actionName, pageRequest);
		
		return new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
	}
	
	private PageDTO processRequest(String pageName, String actionName, PageRequestDTO pageRequest) {
		String operation = pageRequest.getHeader().getOperation();

		//get  the page navigation based for current page
		PageNavigatorDTO pageNavDto = pageNavigationService.getNavPage(actionName, operation, pageName);
		
		//Process current page
		PageDTO pageResponse = pageHandlerFactory.getPageHandler(pageName).handleAppAction(actionName, pageRequest,pageNavDto);
			
		//save data in master Tables (including the previous unsaved data
		String commitMasterData = pageNavDto.getCommitMasterData();
		if("Y".equals(commitMasterData))
		{
			List<PageNavigatorDTO> pagenNavList =  pageNavigationService.getNavList(actionName, operation, pageName);
			for(PageNavigatorDTO item:pagenNavList)
			{
				pageHandlerFactory.getPageHandler(item.getCurrentPage()).saveAsMaster(actionName, pageRequest);
			}
		}
		String nextPageName="";
		//Load next page.. If action is next load the next page.. If action is previous load the previous page.
		if(PageAction.next.toString().equals(actionName) || PageAction.save.toString().equals(actionName))
		{
			nextPageName = pageNavDto.getNextpage();
		}
		else if(PageAction.previous.toString().equals(actionName) || PageAction.back.toString().equals(actionName))
		{
			nextPageName = pageNavDto.getPreviousPage();

		}
		else 
		{
			nextPageName = pageName;
		}
		
		if(!pageName.equals(nextPageName)) {
			PageNavigationContext pageNavigationContext = new PageNavigationContext();
			pageNavigationContext.setActionName(actionName);
			pageNavigationContext.setPageRequest(pageRequest);
			pageNavigationContext.setPreviousPageResponse(pageResponse);
			pageNavigationContext.setPreviousPage(nextPageName);
			//load data for the next page
			pageResponse = pageHandlerFactory.getPageHandler(nextPageName).loadPage(pageNavigationContext);
		}
		else
		{
			PageNavigationContext pageNavigationContext = new PageNavigationContext();
			pageNavigationContext.setActionName(actionName);
			pageNavigationContext.setPageRequest(pageRequest);
			pageNavigationContext.setPreviousPageResponse(pageResponse);
			pageNavigationContext.setPreviousPage(pageName);
			//load data for the same page
			pageResponse = pageHandlerFactory.getPageHandler(pageName).loadPage(pageNavigationContext);
		}
		
		addSuccessHeader(pageName, actionName, operation,pageResponse);
		
		// attach UI controls for the next page to the response
		addUIControls(nextPageName,pageResponse);
		return pageResponse;
	}
	
	private void addSuccessHeader(String currentPageName, String actionName, String operation, PageDTO page) {

		if(page.getHeader() == null) {
			page.setHeader(new HeaderDTO());
		}
		page.getHeader().setRequestStatus(RequestStatusType.S);
		page.getHeader().setPageName(page.getPageName());
		
		//String nextPageName = appConfig.getPageNavigationConfig().get(currentPageName.toLowerCase()).get(actionName.toLowerCase());
		PageNavigatorDTO pageNavDto = pageNavigationService.getNavPage(actionName, operation, currentPageName);
		String nextPageName = pageNavDto.getNextpage();
		page.getHeader().setPageName(nextPageName);
	}
	
	private void addUIControls(String pageName,PageDTO page )
	{
		List<UIControlsDTO> uiControls= uiControlService.get(pageName);
		page.setScreenProperties(uiControls);
	}

	
}
