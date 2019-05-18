package com.enewschamp.domain.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;

@Component
public class PageBuilderFactory {

	@Autowired
	private EnewschampApplicationProperties appConfig; 
	
	@Autowired
	private ApplicationContext context;
	
	public IPageBuilder getPageBuilder(String pageName, String actionName) {
		IPageBuilder builder = null;
		
		// Read next page from properties file based on current page and action. 
		// TODO: This needs to be read from database table which maintains the page navigation
		String nextPageName = appConfig.getPageNavigationConfig().get(pageName.toLowerCase()).get(actionName.toLowerCase());
		
		// Read page builder class for the next page to be returned
		String builderName = appConfig.getPageBuilderConfig().get(nextPageName.toLowerCase());
		
		// Read the bean for next page builder
		builder = (IPageBuilder) context.getBean(builderName);
		
		return builder;
	}
}
