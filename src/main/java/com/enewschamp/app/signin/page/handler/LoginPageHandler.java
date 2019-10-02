package com.enewschamp.app.signin.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.login.service.StudentLoginBusiness;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "LoginPageHandler")
public class LoginPageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentRegistrationBusiness studentRegBusiness;

	@Autowired
	StudentLoginBusiness studentLoginBusiness;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();

		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		LoginPageData loginPageData = new LoginPageData();

		loginPageData = modelMapper.map(pageNavigationContext.getPreviousPageResponse().getData(),
				LoginPageData.class);
		loginPageData.setPassword("");
		pageDto.setData(loginPageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {

		return null;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		LoginPageData loginPageData = new LoginPageData();
		String emailId = "";
		String password = "";
		String deviceId = "";
		boolean loginSuccess = false;
		if (PageAction.login.toString().equalsIgnoreCase(action)) {
			// String emailId = pageRequest.getData().
			try {
				loginPageData = objectMapper.readValue(pageRequest.getData().toString(), LoginPageData.class);
				emailId = loginPageData.getEmailId();
				password = loginPageData.getPassword();
				deviceId = loginPageData.getDeviceId();

			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			loginSuccess = studentRegBusiness.validatePassword(emailId, password);
			if (loginSuccess) {
				studentLoginBusiness.login(emailId, deviceId);
			} else {

				throw new BusinessException(ErrorCodes.INVALID_EMAILID_OR_PASSWORD, "Invalid User Id Or Password");
			}
			pageDto.setData(loginPageData);
		}
		if (PageAction.logout.toString().equalsIgnoreCase(action)) {
			try {
				loginPageData = objectMapper.readValue(pageRequest.getData().toString(), LoginPageData.class);
				emailId = loginPageData.getEmailId();
				deviceId = loginPageData.getDeviceId();

			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			studentLoginBusiness.logout(emailId, deviceId);
			pageDto.setData(loginPageData);

		}

		return pageDto;
	}

}
