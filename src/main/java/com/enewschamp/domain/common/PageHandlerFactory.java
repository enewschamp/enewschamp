package com.enewschamp.domain.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.problem.BusinessException;

@Component
public class PageHandlerFactory {

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	private ApplicationContext context;

	public IPageHandler getPageHandler(String pageName, String context) {

		String pageHandlerName = "";
		if ("app".equalsIgnoreCase(context)) {
			pageHandlerName = pageName + "PageHandler";
		} else {
			pageHandlerName = appConfig.getPageHandlerConfig().get(context).get(pageName.toLowerCase());
		}
		if (pageHandlerName == null) {
			throw new BusinessException(ErrorCodeConstants.PAGE_NOT_FOUND,
					"Page handler not found for page: " + pageName);
		}
		System.out.println(">>>>>pageHandlerName>>>>>>>" + pageHandlerName);
		return getPageHandlerBean(pageHandlerName);
	}

	private IPageHandler getPageHandlerBean(String pageHandlerName) {
		return (IPageHandler) context.getBean(pageHandlerName);
	}
}
