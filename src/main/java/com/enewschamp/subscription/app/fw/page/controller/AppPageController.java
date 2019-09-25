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

import com.enewschamp.EnewschampApplicationProperties;
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
import com.enewschamp.publication.domain.service.EditionService;

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
	
	@Autowired
	EditionService editionService;
	@Autowired
	EnewschampApplicationProperties appConfig;

	
	@PostMapping(value = "/app")
	public ResponseEntity<PageDTO> processAppRequest(@RequestBody PageRequestDTO pageRequest) {
		
		String pageName = pageRequest.getHeader().getPageName();
		String actionName = pageRequest.getHeader().getAction();
		pageRequest.getHeader().setPageName(pageName);
		pageRequest.getHeader().setAction(actionName);
		
		PageDTO pageResponse = processRequest(pageName, actionName, pageRequest,"app");
		
		return new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
	}
	
	private PageDTO processRequest(String pageName, String actionName, PageRequestDTO pageRequest, String context) {
		String operation = pageRequest.getHeader().getOperation();
		String edition = pageRequest.getHeader().getEditionID();
		//check if the edition exist..
		editionService.getEdition(edition);
		
		//set default page size from app..
		pageRequest.getHeader().setPageSize(appConfig.getPageSize());
		
		//get  the page navigation based for current page
		PageNavigatorDTO pageNavDto = pageNavigationService.getNavPage(actionName, operation, pageName);
		
		//Process current page
		//PageDTO pageResponse = pageHandlerFactory.getPageHandler(pageName).handleAppAction(actionName, pageRequest,pageNavDto);
		PageDTO pageResponse = pageHandlerFactory.getPageHandler(pageName, context).handleAppAction(actionName, pageRequest,pageNavDto);

		//save data in master Tables (including the previous unsaved data
		String commitMasterData = pageNavDto.getCommitMasterData();
		if("Y".equals(commitMasterData))
		{
			List<PageNavigatorDTO> pagenNavList =  pageNavigationService.getNavList(actionName, operation, pageName);
			for(PageNavigatorDTO item:pagenNavList)
			{
				pageHandlerFactory.getPageHandler(item.getCurrentPage(),context).saveAsMaster(actionName, pageRequest);
			}
		}
		String nextPageName="";
		//Load next page.. If action is next load the next page.. If action is previous load the previous page.
		if(PageAction.next.toString().equalsIgnoreCase(actionName) || PageAction.save.toString().equalsIgnoreCase(actionName) || PageAction.home.toString().equalsIgnoreCase(actionName) || PageAction.clickArticleImage.toString().equalsIgnoreCase(actionName) || PageAction.savedarticles.toString().equalsIgnoreCase(actionName) || PageAction.GoPremium.toString().equalsIgnoreCase(actionName) || PageAction.PicImage.toString().equalsIgnoreCase(actionName) || PageAction.opinions.toString().equalsIgnoreCase(actionName) || PageAction.subscription.toString().equalsIgnoreCase(actionName) || PageAction.savednewsarticle.toString().equalsIgnoreCase(actionName) || PageAction.MyActivity.toString().equalsIgnoreCase(actionName) || PageAction.Champs.toString().equalsIgnoreCase(actionName) || PageAction.Menu.toString().equalsIgnoreCase(actionName) || PageAction.HelpDesk.toString().equalsIgnoreCase(actionName) 
				|| PageAction.Month.toString().equalsIgnoreCase(actionName) || PageAction.Year.toString().equalsIgnoreCase(actionName) || PageAction.Cancel.toString().equalsIgnoreCase(actionName) || PageAction.MyProfile.toString().equalsIgnoreCase(actionName) || PageAction.FilterSavedArticles.toString().equalsIgnoreCase(actionName) || PageAction.ClearFilterSavedArticles.toString().equalsIgnoreCase(actionName)  || PageAction.Scores.toString().equalsIgnoreCase(actionName) 
				|| PageAction.Trends.toString().equalsIgnoreCase(actionName)|| PageAction.recognitions.toString().equalsIgnoreCase(actionName) || PageAction.LeftSwipe.toString().equalsIgnoreCase(actionName) || PageAction.RightSwipe.toString().equalsIgnoreCase(actionName))
		{
			nextPageName = pageNavDto.getNextpage();
		}
		else if(PageAction.previous.toString().equalsIgnoreCase(actionName) || PageAction.back.toString().equalsIgnoreCase(actionName))
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
			pageResponse = pageHandlerFactory.getPageHandler(nextPageName,context).loadPage(pageNavigationContext);
		}
		else
		{
			PageNavigationContext pageNavigationContext = new PageNavigationContext();
			pageNavigationContext.setActionName(actionName);
			pageNavigationContext.setPageRequest(pageRequest);
			pageNavigationContext.setPreviousPageResponse(pageResponse);
			pageNavigationContext.setPreviousPage(pageName);
			//load data for the same page
			pageResponse = pageHandlerFactory.getPageHandler(pageName,context).loadPage(pageNavigationContext);
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
