package com.enewschamp.app.publisher.service;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.signin.page.handler.SignInPageData;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "PublisherLoginPageHandler")
public class PublisherLoginPageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	UserService userService;

	@Autowired
	UserLoginBusiness userLoginBusiess;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();

		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		PublisherLoginPageData loginPageData = new PublisherLoginPageData();

		loginPageData = modelMapper.map(pageNavigationContext.getPreviousPageResponse().getData(),
				PublisherLoginPageData.class);
		loginPageData.setPassword("");
		pageDto.setData(loginPageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {

		return null;
	}

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		PublisherLoginPageData loginPageData = new PublisherLoginPageData();
		String userId = "";
		String password = "";
		String deviceId = "";
		boolean loginSuccess = false;
		if (PageAction.login.toString().equalsIgnoreCase(action)) {
			try {
				loginPageData = objectMapper.readValue(pageRequest.getData().toString(), PublisherLoginPageData.class);
				userId = pageRequest.getHeader().getUserId();
				password = loginPageData.getPassword();
				deviceId = pageRequest.getHeader().getDeviceId();

			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			loginSuccess = userService.validatePassword(userId, password);
			if (loginSuccess) {
				userLoginBusiess.login(userId, deviceId, UserType.A);
			} else {
				throw new BusinessException(ErrorCodes.INVALID_USERNAME_OR_PASSWORD, userId);
			}
			pageDto.setData(loginPageData);
		}
		if (PageAction.logout.toString().equalsIgnoreCase(action)) {
			try {
				loginPageData = objectMapper.readValue(pageRequest.getData().toString(), PublisherLoginPageData.class);
				userId = pageRequest.getHeader().getUserId();
				deviceId = pageRequest.getHeader().getDeviceId();

			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			userLoginBusiess.logout(userId, deviceId, UserType.A);
			pageDto.setData(loginPageData);
		}
		
		if (PageAction.ResetPassword.toString().equalsIgnoreCase(action)) {
			userId = pageRequest.getHeader().getUserId();
			deviceId = pageRequest.getHeader().getDeviceId();
			String passwordNew, passwordRepeat = null;
			try {
				loginPageData = objectMapper.readValue(pageRequest.getData().toString(), PublisherLoginPageData.class);
				password = loginPageData.getPassword();
				passwordNew = loginPageData.getPasswordNew();
				passwordRepeat = loginPageData.getPasswordRepeat();
			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (password == null && "".equals(password)) {
				throw new BusinessException(ErrorCodes.INVALID_USER_PASSWORD);
			}
			if (passwordNew == null && "".equals(passwordNew)) {
				throw new BusinessException(ErrorCodes.INVALID_USER_NEW_PASSWORD);
			}
			if (passwordRepeat == null && "".equals(passwordRepeat)) {
				throw new BusinessException(ErrorCodes.INVALID_USER_NEW_PASSWORD);
			}
			if (passwordNew.equals(password)) {
				throw new BusinessException(ErrorCodes.OLD_NEW_PASSWORD_SAME);
			}
			boolean validPassword = userService.validatePassword(userId, password);
			if (!validPassword) {
				throw new BusinessException(ErrorCodes.INVALID_USERNAME_OR_PASSWORD, userId);
			}
			
			if (!passwordNew.equals(passwordRepeat)) {
				throw new BusinessException(ErrorCodes.BOTH_PASSWORD_DO_NOT_MATCH);
			}
			userService.resetPassword(userId, passwordNew);
			userLoginBusiess.logout(userId, deviceId, UserType.A);
		}
		return pageDto;
	}

}
