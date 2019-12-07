package com.enewschamp.domain.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.problem.BusinessException;

@Component
public class PageHandlerFactory {

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	private ApplicationContext context;

	public IPageHandler getPageHandler(String pageName, String context) {

		String pageHandlerName = appConfig.getPageHandlerConfig().get(context).get(pageName.toLowerCase());

		if (pageHandlerName == null) {
			throw new BusinessException(ErrorCodes.PAGE_NOT_FOUND, "Page handler not found for page: " + pageName);
		}
		System.out.println(">>>>>pageHandlerName>>>>>>>" + pageHandlerName);
		return getPageHandlerBean(pageHandlerName);
	}

//	public IPageHandler getNextPageHandler(String currentPageName, String actionName) {
//
//		// Read next page from properties file based on current page and action. 
//		// TODO: This needs to be read from database table which maintains the page navigation
//		String nextPageName = appConfig.getPageNavigationConfig().get(currentPageName.toLowerCase()).get(actionName.toLowerCase());
//		
//		// Read page handler class for the next page to be returned
//		String pageHandlerName = appConfig.getPageHandlerConfig().get(nextPageName.toLowerCase());
//		
//		if(pageHandlerName == null) {
//			throw new RuntimeException("Page handler not found for page: " + currentPageName);
//		}
//		
//		return getPageHandlerBean(pageHandlerName);
//	}

	private IPageHandler getPageHandlerBean(String pageHandlerName) {
		return (IPageHandler) context.getBean(pageHandlerName);
	}
}
