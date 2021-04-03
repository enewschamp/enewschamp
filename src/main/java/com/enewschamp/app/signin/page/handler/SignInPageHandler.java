package com.enewschamp.app.signin.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.apache.commons.lang3.exception.ExceptionUtils;
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
import com.enewschamp.common.domain.service.ErrorCodesService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
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
	ErrorCodesService errorCodeService;

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
	PropertiesBackendService propertiesService;

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
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
					// e.printStackTrace();
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
		SignInPageData signInPageData = new SignInPageData();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		String deviceId = pageRequest.getHeader().getDeviceId();
		String emailId = "";
		try {
			signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
			emailId = signInPageData.getEmailId();
			pageDto.getHeader().setEmailId(emailId);
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
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("" + studentId);
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(
				pageRequest.getHeader().getPageName() + "-" + pageRequest.getHeader().getOperation() + "-" + action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.S);
		userActivityTracker.setActionTime(LocalDateTime.now());
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		if (student != null && "Y".equals(student.getIsDeleted())) {
			userActivityTracker.setErrorCode(ErrorCodeConstants.STUD_ACCOUNT_DELETED);
			userActivityTracker.setErrorDescription(errorCodeService.getValue(ErrorCodeConstants.STUD_ACCOUNT_DELETED));
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.STUD_ACCOUNT_DELETED, emailId);
		} else if (studentControlDTO != null && "Y".equals(studentControlDTO.getEmailIdVerified())) {
			userActivityTracker.setErrorCode(ErrorCodeConstants.STUD_ALREADY_REGISTERED);
			userActivityTracker
					.setErrorDescription(errorCodeService.getValue(ErrorCodeConstants.STUD_ALREADY_REGISTERED));
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.STUD_ALREADY_REGISTERED);
		}
		if (student != null) {
			pageDto.getHeader().setStudentKey(student.getStudentKey());
		}
		return pageDto;
	}

	public PageDTO handleDeleteAccountAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		SignInPageData signInPageData = new SignInPageData();
		String emailId = pageRequest.getHeader().getEmailId();
		String module = pageRequest.getHeader().getModule();
		String password = "";
		String deviceId = pageRequest.getHeader().getDeviceId();
		String tokenId = pageRequest.getHeader().getLoginCredentials();
		StudentRegistration student = regService.getStudentReg(emailId);
		Long studentId = 0L;
		if (student != null) {
			studentId = student.getStudentId();
		}
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("" + studentId);
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(
				pageRequest.getHeader().getPageName() + "-" + pageRequest.getHeader().getOperation() + "-" + action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.S);
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
		boolean loginSuccess = studentRegBusiness.validatePassword(module, emailId, password, deviceId, tokenId,
				userActivityTracker);
		if (loginSuccess) {
			UserLogin userLogin = loginService.getDeviceLogin(emailId, deviceId, tokenId, UserType.S);
			userLogin.setLoginFlag(AppConstants.NO);
			loginService.update(userLogin);
		} else {
			throw new BusinessException(ErrorCodeConstants.INVALID_EMAILID_OR_PASSWORD);
		}
		studentRegBusiness.deleteAccount(emailId);
		signInPageData.setMessage(propertiesService.getValue(module, PropertyConstants.ACCOUNT_DELETION_MESSAGE));
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		userLoginBusiness.auditUserActivity(userActivityTracker);
		pageDto.setData(signInPageData);
		return pageDto;
	}

	public PageDTO handleSignInAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		String emailId = "";
		String password = "";
		String fcmToken = "";
		String deviceId = pageRequest.getHeader().getDeviceId();
		String tokenId = pageRequest.getHeader().getLoginCredentials();
		String action = pageRequest.getHeader().getAction();
		String module = pageRequest.getHeader().getModule();
		String appVersion = pageRequest.getHeader().getAppVersion();
		SignInPageData signInPageData = new SignInPageData();
		boolean loginSuccess = false;
		try {
			signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
			emailId = signInPageData.getEmailId();
			password = signInPageData.getPassword();
			fcmToken = signInPageData.getFcmToken();
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
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("" + studentId);
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(
				pageRequest.getHeader().getPageName() + "-" + pageRequest.getHeader().getOperation() + "-" + action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.S);
		userActivityTracker.setActionTime(LocalDateTime.now());
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		if (studentControlDTO == null) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userActivityTracker.setErrorCode(ErrorCodeConstants.STUD_REG_NOT_FOUND);
			userActivityTracker.setErrorDescription(errorCodeService.getValue(ErrorCodeConstants.STUD_REG_NOT_FOUND));
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.STUD_REG_NOT_FOUND, emailId);
		}
		loginSuccess = studentRegBusiness.validatePassword(module, emailId, password, deviceId, tokenId,
				userActivityTracker);
		if (loginSuccess) {
			UserLogin userLogin = userLoginBusiness.login(emailId, "" + studentId, deviceId, tokenId, module,
					appVersion, UserType.S);
			userLogin.setLoginFlag(AppConstants.YES);
			userLogin.setUserId(emailId);
			loginService.update(userLogin);
			StudentRegistration studeReg = regService.getStudentReg(emailId);
			studeReg.setFcmToken(fcmToken);
			regService.update(studeReg);
			pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
			pageRequest.getHeader().setEmailId(emailId);
			pageRequest.getHeader().setStudentKey(studeReg.getStudentKey());
		} else {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userActivityTracker.setErrorCode(ErrorCodeConstants.INVALID_EMAILID_OR_PASSWORD);
			userActivityTracker
					.setErrorDescription(errorCodeService.getValue(ErrorCodeConstants.INVALID_EMAILID_OR_PASSWORD));
			userLoginBusiness.auditUserActivity(userActivityTracker);
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
		SignInPageData signInPageData = new SignInPageData();
		String emailId = "";
		try {
			signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
			emailId = signInPageData.getEmailId();
			pageRequest.getHeader().setEmailId(emailId);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String deviceId = pageRequest.getHeader().getDeviceId();
		StudentRegistration student = regService.getStudentReg(emailId);
		Long studentId = 0L;
		if (student != null) {
			studentId = student.getStudentId();
		}
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("" + studentId);
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(
				pageRequest.getHeader().getPageName() + "-" + pageRequest.getHeader().getOperation() + "-" + action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.S);
		userActivityTracker.setActionTime(LocalDateTime.now());
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		if ((student != null && "Y".equals(student.getIsDeleted()))
				|| (studentControlDTO == null || !"Y".equals(studentControlDTO.getEmailIdVerified()))) {
			throw new BusinessException(ErrorCodeConstants.STUD_REG_NOT_FOUND, emailId);
		}
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		userLoginBusiness.auditUserActivity(userActivityTracker);
		return pageDto;
	}

	public PageDTO handleUnlockAccountAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		SignInPageData signInPageData = new SignInPageData();
		String emailId = "";
		try {
			signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
			emailId = signInPageData.getEmailId();
			pageRequest.getHeader().setEmailId(emailId);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String deviceId = pageRequest.getHeader().getDeviceId();
		StudentRegistration student = regService.getStudentReg(emailId);
		Long studentId = 0L;
		if (student != null) {
			studentId = student.getStudentId();
		}
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("" + studentId);
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(
				pageRequest.getHeader().getPageName() + "-" + pageRequest.getHeader().getOperation() + "-" + action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.S);
		userActivityTracker.setActionTime(LocalDateTime.now());
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		if ((student != null && "Y".equals(student.getIsDeleted()))
				|| (studentControlDTO == null || !"Y".equals(studentControlDTO.getEmailIdVerified()))) {
			throw new BusinessException(ErrorCodeConstants.STUD_REG_NOT_FOUND, emailId);
		}
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		userLoginBusiness.auditUserActivity(userActivityTracker);
		return pageDto;
	}
}