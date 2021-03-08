package com.enewschamp.publication.page.handler;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.signin.page.handler.LoginPageData;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.app.user.login.service.UserLoginService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.AbstractPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.user.domain.entity.User;
import com.enewschamp.user.domain.service.UserService;

@Component(value = "PublisherMenuPageHandler")
public class PublisherMenuPageHandler extends AbstractPageHandler {

	@Autowired
	private PropertiesBackendService propertiesService;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	UserService userService;

	@Autowired
	UserLoginService userLoginService;

	@Override
	public PageDTO handlePageAction(PageRequestDTO pageRequest) {
		PageDTO page = new PageDTO();
		page.setHeader(pageRequest.getHeader());
		return page;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String userId = pageNavigationContext.getPageRequest().getHeader().getUserId();
		User user = userService.get(userId);
		LoginPageData loginPageData = new LoginPageData();
		if (user != null) {
			loginPageData.setUserName(user.getName() + " " + user.getSurname());
			loginPageData.setTodaysDate(LocalDate.now());
			loginPageData.setTheme(user.getTheme());
			loginPageData.setImageName(user.getImageName());
			loginPageData.setUserRole(userLoginBusiness.getUserRole(userId));
			loginPageData.setLoginCredentials(userLoginService.getOperatorLogin(userId).getTokenId());
			loginPageData.setTokenValidity(
					propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
							PropertyConstants.LOGIN_SESSION_EXPIRY_SECS));
		}
		pageDto.setData(loginPageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO page = new PageDTO();
		page.setHeader(pageRequest.getHeader());
		return page;
	}
}
