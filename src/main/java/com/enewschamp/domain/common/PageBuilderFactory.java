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
		
		String nextPageName = appConfig.getPageNavigationConfig().get(pageName.toLowerCase()).get(actionName.toLowerCase());
		
		String builderName = appConfig.getPageBuilderConfig().get(nextPageName.toLowerCase());
		builder = (IPageBuilder) context.getBean(builderName);
		return builder;
	}
}
