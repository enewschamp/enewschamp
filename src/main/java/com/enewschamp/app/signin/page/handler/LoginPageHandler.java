package com.enewschamp.app.signin.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
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
	StudentRegistrationService regService;

	@Autowired
	UserLoginBusiness userLoginBusiess;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		LoginPageData loginPageData = new LoginPageData();
		loginPageData = modelMapper.map(pageNavigationContext.getPreviousPageResponse().getData(), LoginPageData.class);
		loginPageData.setPassword("");
		pageDto.setData(loginPageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {

		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		String methodName = pageNavigatorDTO.getSubmissionMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[2];
			params[0] = PageRequestDTO.class;
			params[1] = PageNavigatorDTO.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
				}
			} catch (NoSuchMethodException nsmEx) {
				nsmEx.printStackTrace();
			} catch (SecurityException seEx) {
				seEx.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		return pageDTO;
	}

	// method not in use - to be removed
	public PageDTO handleLoginAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		LoginPageData loginPageData = new LoginPageData();
		String userId = "";
		String password = "";
		String deviceId = pageRequest.getHeader().getDeviceId();
		String tokenId = pageRequest.getHeader().getLoginCredentials();
		String module = pageRequest.getHeader().getModule();
		String appVersion = pageRequest.getHeader().getAppVersion();
		String emailId = "";
		boolean loginSuccess = false;
		try {
			loginPageData = objectMapper.readValue(pageRequest.getData().toString(), LoginPageData.class);
			password = loginPageData.getPassword();
			emailId = loginPageData.getEmailId();
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		StudentRegistration student = regService.getStudentReg(emailId);
		Long studentId = 0L;
		if (student != null) {
			studentId = student.getStudentId();
		}
		loginSuccess = studentRegBusiness.validatePassword(module, userId, password, deviceId, tokenId, null);
		if (loginSuccess) {
			userLoginBusiess.login(userId, "" + studentId, deviceId, "", module, appVersion, UserType.S);
		} else {
			throw new BusinessException(ErrorCodeConstants.INVALID_EMAILID_OR_PASSWORD);
		}
		pageDto.setData(loginPageData);

		return pageDto;
	}

	// method not in use - to be removed
	public PageDTO handleLogoutAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		LoginPageData loginPageData = new LoginPageData();
		String userId = "";
		String deviceId = "";
		try {
			loginPageData = objectMapper.readValue(pageRequest.getData().toString(), LoginPageData.class);
			userId = loginPageData.getEmailId();
			deviceId = loginPageData.getDeviceId();
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		userLoginBusiess.logout(userId, deviceId, "", UserType.S);
		pageDto.setData(loginPageData);
		return pageDto;
	}

}