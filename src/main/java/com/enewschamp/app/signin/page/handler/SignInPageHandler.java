package com.enewschamp.app.signin.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.app.user.login.entity.UserAction;
import com.enewschamp.app.user.login.entity.UserActivityTracker;
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.app.user.login.service.UserLoginService;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "SignInPageHandler")
public class SignInPageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	UserService userService;

	@Autowired
	StudentRegistrationService regService;

	@Autowired
	StudentRegistrationBusiness studentRegBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	UserLoginService loginService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	PropertiesService propertiesService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
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
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			try {
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;

	}

	public PageDTO handleCreateAccountAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		String emailId = pageRequest.getHeader().getEmailId();
		String deviceId = pageRequest.getHeader().getDeviceId();
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("SYSTEM");
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.P);
		userActivityTracker.setActionTime(LocalDateTime.now());
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		userLoginBusiness.auditUserActivity(userActivityTracker);
		StudentRegistration student = regService.getStudentReg(emailId);
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		if (student != null && "Y".equals(student.getIsDeleted())) {
			throw new BusinessException(ErrorCodeConstants.STUD_ACCOUNT_DELETED, emailId);
		} else if (studentControlDTO != null && "Y".equals(studentControlDTO.getEmailVerified())) {
			throw new BusinessException(ErrorCodeConstants.STUD_ALREADY_REGISTERED);
		}
		return pageDto;
	}

	public PageDTO handleDeleteAccountAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		SignInPageData signInPageData = new SignInPageData();
		String emailId = pageRequest.getHeader().getEmailId();
		String password = "";
		String deviceId = pageRequest.getHeader().getDeviceId();
		String tokenId = pageRequest.getHeader().getLoginCredentials();
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("SYSTEM");
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.P);
		userActivityTracker.setActionTime(LocalDateTime.now());
		try {
			signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
			password = signInPageData.getPassword();
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		boolean loginSuccess = studentRegBusiness.validatePassword(emailId, password, deviceId, tokenId);
		if (loginSuccess) {
			UserLogin userLogin = loginService.getDeviceLogin(emailId, deviceId, tokenId, UserType.S);
			userLogin.setLoginFlag(AppConstants.NO);
			loginService.update(userLogin);
		} else {
			throw new BusinessException(ErrorCodeConstants.INVALID_EMAILID_OR_PASSWORD);
		}
		studentRegBusiness.deleteAccount(emailId);
		signInPageData.setMessage(propertiesService.getProperty(PropertyConstants.ACCOUNT_DELETION_MESSAGE));
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		userLoginBusiness.auditUserActivity(userActivityTracker);
		pageDto.setData(signInPageData);
		return pageDto;
	}

	public PageDTO handleSignInAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		String action = pageRequest.getHeader().getAction();
		SignInPageData signInPageData = new SignInPageData();
		String emailId = "";
		String password = "";
		String deviceId = "";
		String tokenId = "";
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("SYSTEM");
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.P);
		userActivityTracker.setActionTime(LocalDateTime.now());
		boolean loginSuccess = false;
		try {
			signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
			emailId = pageRequest.getHeader().getEmailId();
			password = signInPageData.getPassword();
			deviceId = pageRequest.getHeader().getDeviceId();
			tokenId = pageRequest.getHeader().getLoginCredentials();
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		if (studentControlDTO == null) {
			throw new BusinessException(ErrorCodeConstants.STUD_REG_NOT_FOUND, emailId);
		}
		loginSuccess = studentRegBusiness.validatePassword(emailId, password, deviceId, tokenId);
		if (loginSuccess) {
			UserLogin userLogin = userLoginBusiness.login(emailId, deviceId, tokenId, UserType.S);
			userLogin.setLoginFlag(AppConstants.YES);
			loginService.update(userLogin);
			pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
		} else {
			throw new BusinessException(ErrorCodeConstants.INVALID_EMAILID_OR_PASSWORD);
		}
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		userLoginBusiness.auditUserActivity(userActivityTracker);
		pageDto.setData(signInPageData);
		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	public PageDTO handleForgotPasswordAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		String emailId = pageRequest.getHeader().getEmailId();
		String deviceId = pageRequest.getHeader().getDeviceId();
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("SYSTEM");
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.P);
		userActivityTracker.setActionTime(LocalDateTime.now());
		StudentRegistration student = regService.getStudentReg(emailId);
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		if ((student != null && "Y".equals(student.getIsDeleted()))
				|| (studentControlDTO == null || !"Y".equals(studentControlDTO.getEmailVerified()))) {
			throw new BusinessException(ErrorCodeConstants.STUD_REG_NOT_FOUND, emailId);
		}
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		userLoginBusiness.auditUserActivity(userActivityTracker);
		return pageDto;
	}
}
