package com.enewschamp.app.about.page.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.about.page.data.AboutUsPageData;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;

@Component(value = "AboutUsPageHandler")
public class AboutUsPageHandler implements IPageHandler {

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();

		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());

		AboutUsPageData aboutUsPageData = new AboutUsPageData();
		aboutUsPageData.setAboutUsText(appConfig.getAboutUsText());
		pageDto.setData(aboutUsPageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());

		// TODO Auto-generated method stub
		return pageDto;
	}

}
