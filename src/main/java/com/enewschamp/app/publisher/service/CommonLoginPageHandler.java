package com.enewschamp.app.publisher.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.otp.entity.OTP;
import com.enewschamp.app.otp.repository.OTPRepository;
import com.enewschamp.app.otp.service.OTPService;
import com.enewschamp.app.user.login.entity.UserAction;
import com.enewschamp.app.user.login.entity.UserActivityTracker;
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.app.user.login.service.UserLoginService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "CommonLoginPageHandler")
public class CommonLoginPageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	UserService userService;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	OTPService otpService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	OTPRepository otpRepository;

	@Autowired
	PropertiesBackendService propertiesService;

	@Autowired
	UserLoginService loginService;

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		PublisherLoginPageData loginPageData = new PublisherLoginPageData();

		loginPageData = modelMapper.map(pageNavigationContext.getPreviousPageResponse().getData(),
				PublisherLoginPageData.class);
		pageDto.setData(loginPageData);
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		PublisherLoginPageData loginPageData = new PublisherLoginPageData();
		String userId = pageRequest.getHeader().getUserId();
		String deviceId = pageRequest.getHeader().getDeviceId();
		String tokenId = pageRequest.getHeader().getLoginCredentials();
		String appVersion = pageRequest.getHeader().getAppVersion();
		String module = pageRequest.getHeader().getModule();
		String password = "";
		UserType userType = pageRequest.getHeader().getModule().equals("Admin") ? UserType.A : UserType.P;
		boolean loginSuccess = false;
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("SYSTEM");
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(userId);
		userActivityTracker.setUserType(userType);
		userActivityTracker.setActionTime(LocalDateTime.now());
		if (null == userId || "".equals(userId) || !userService.validateUser(userId)) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.INVALID_USER_ID, userId);
		}

		if ("login".equalsIgnoreCase(action)) {
			try {
				loginPageData = objectMapper.readValue(pageRequest.getData().toString(), PublisherLoginPageData.class);
				password = loginPageData.getPassword();
			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			loginSuccess = userService.validatePassword(userId, password, userActivityTracker);
			if (loginSuccess) {
				UserLogin userLogin = userLoginBusiness.login(userId, userId, deviceId, tokenId, module, appVersion,
						UserType.A);
				userLogin.setLoginFlag(AppConstants.YES);
				loginService.update(userLogin);
				userActivityTracker.setActionStatus(UserAction.SUCCESS);
				userLoginBusiness.auditUserActivity(userActivityTracker);
			} else {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.INVALID_USERNAME_OR_PASSWORD, userId);
			}
			pageDto.setData(loginPageData);
		} else if ("logout".equalsIgnoreCase(action)) {
			try {
				loginPageData = objectMapper.readValue(pageRequest.getData().toString(), PublisherLoginPageData.class);
			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			userLoginBusiness.isUserLoggedIn(deviceId, tokenId, userId, userType, userActivityTracker);
			userLoginBusiness.logout(userId, deviceId, tokenId, userType);
			userActivityTracker.setActionStatus(UserAction.SUCCESS);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			loginPageData = new PublisherLoginPageData();
			loginPageData.setMessage("User Logout Successfully");
			pageDto.setData(loginPageData);
		} else if ("forgotpassword".equalsIgnoreCase(action)) {
			String emailId = userService.get(userId).getEmailId1();
			if (emailId == null && "".equals(emailId)) {
				throw new BusinessException(ErrorCodeConstants.INVALID_EMAIL_ID, emailId);
			}
			loginPageData.setMessage(
					propertiesService.getValue(pageRequest.getHeader().getModule(), PropertyConstants.OTP_MESSAGE));
			otpService.genOTP(null, pageRequest.getHeader().getModule(), emailId, userActivityTracker);
			userActivityTracker.setActionStatus(UserAction.SUCCESS);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			loginPageData = new PublisherLoginPageData();
			String maskedEmail = maskedEmailAddress(emailId);
			loginPageData.setMessage("OTP Sent Successfully On Email " + maskedEmail);
			loginPageData.setEmailId(maskedEmail);
			pageDto.setData(loginPageData);
		} else if ("resendsecuritycode".equalsIgnoreCase(action)) {
			String emailId = userService.get(userId).getEmailId1();
			if (emailId == null && "".equals(emailId)) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.INVALID_EMAIL_ID);
			}
			loginPageData.setMessage(
					propertiesService.getValue(pageRequest.getHeader().getModule(), PropertyConstants.OTP_MESSAGE));
			List<OTP> otpGenList = otpRepository.getOtpGenListForEmail(emailId, RecordInUseType.Y);
			if (otpGenList != null && otpGenList.size() >= Integer.valueOf(propertiesService
					.getValue(pageRequest.getHeader().getModule(), PropertyConstants.OTP_GEN_MAX_ATTEMPTS))) {
				throw new BusinessException(ErrorCodeConstants.OTP_GEN_MAX_ATTEMPTS_EXHAUSTED);
			}
			otpService.genOTP(null, pageRequest.getHeader().getModule(), emailId, userActivityTracker);
			userActivityTracker.setActionStatus(UserAction.SUCCESS);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			loginPageData = new PublisherLoginPageData();
			String maskedEmail = maskedEmailAddress(emailId);
			loginPageData.setMessage("OTP Sent Successfully On Email " + maskedEmail);
			loginPageData.setEmailId(maskedEmail);
			pageDto.setData(loginPageData);
		} else if ("resetpassword".equalsIgnoreCase(action)) {
			String passwordNew, passwordRepeat, securityCode = null;
			try {
				loginPageData = objectMapper.readValue(pageRequest.getData().toString(), PublisherLoginPageData.class);
				securityCode = loginPageData.getSecurityCode();
				passwordNew = loginPageData.getPasswordNew();
				passwordRepeat = loginPageData.getPasswordRepeat();
			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (passwordNew == null && "".equals(passwordNew)) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.INVALID_USER_NEW_PASSWORD);
			} else if (passwordRepeat == null && "".equals(passwordRepeat)) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.INVALID_USER_NEW_PASSWORD);
			} else if (!passwordNew.equals(passwordRepeat)) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.BOTH_PASSWORD_DO_NOT_MATCH);
			}
			if (securityCode == null) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.INVALID_SECURITY_CODE);
			}
			String emailId = userService.get(userId).getEmailId1();
			if (emailId == null && "".equals(emailId)) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.INVALID_EMAIL_ID);
			}
			boolean validOtp = otpService.validateOtp(pageRequest.getHeader().getModule(), securityCode, emailId,
					userActivityTracker);
			if (!validOtp) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.INVALID_SECURITY_CODE);
			}
			userService.resetPassword(userId, passwordNew, userActivityTracker);
			userLoginBusiness.logout(userId, deviceId, tokenId, userType);
			userActivityTracker.setActionStatus(UserAction.SUCCESS);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			loginPageData = new PublisherLoginPageData();
			loginPageData.setMessage("Password Reset Successfully");
			pageDto.setData(loginPageData);
		} else if ("changepassword".equalsIgnoreCase(action)) {
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
			userLoginBusiness.isUserLoggedIn(deviceId, tokenId, userId, userType, userActivityTracker);
			if (password == null && "".equals(password)) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.INVALID_USER_PASSWORD);
			} else if (passwordNew == null && "".equals(passwordNew)) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.INVALID_USER_NEW_PASSWORD);
			} else if (passwordRepeat == null && "".equals(passwordRepeat)) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.INVALID_USER_NEW_PASSWORD);
			} else if (passwordNew.equals(password)) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.OLD_NEW_PASSWORD_SAME);
			}
			boolean validPassword = userService.validatePassword(userId, password, userActivityTracker);
			if (!validPassword) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.INVALID_USERNAME_OR_PASSWORD, userId);
			} else if (!passwordNew.equals(passwordRepeat)) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.BOTH_PASSWORD_DO_NOT_MATCH);
			}
			userService.changePassword(userId, passwordNew, userActivityTracker);
			userActivityTracker.setActionStatus(UserAction.SUCCESS);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			loginPageData = new PublisherLoginPageData();
			loginPageData.setMessage("Password Changed Successfully");
			pageDto.setData(loginPageData);
		} else if ("changetheme".equalsIgnoreCase(action)) {
			String theme = null;
			try {
				loginPageData = objectMapper.readValue(pageRequest.getData().toString(), PublisherLoginPageData.class);
				theme = loginPageData.getTheme();
			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			userLoginBusiness.isUserLoggedIn(deviceId, tokenId, userId, userType, userActivityTracker);
			if (theme == null && "".equals(theme)) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.INVALID_THEME);
			}
			userService.changeTheme(userId, theme, userActivityTracker);
			userActivityTracker.setActionStatus(UserAction.SUCCESS);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			loginPageData = new PublisherLoginPageData();
			loginPageData.setMessage("Theme Changed Successfully.");
			pageDto.setData(loginPageData);
		}
		return pageDto;
	}

	public static String maskedEmailAddress(String email) {
		String emailAddress = email.split("@")[0];
		String emailDomain = email.split("@")[1];
		int totalLength = emailAddress.length();
		int maskLength = 0;
		if (totalLength > 2) {
			maskLength = (int) Math.floor((totalLength - 2) * 60 / 100) + 1;
		}
		if (maskLength > 0) {
			String maskStr = "";
			for (int i = 0; i < maskLength; i++) {
				maskStr += "*";
			}
			emailAddress = emailAddress.substring(0, 1) + maskStr
					+ emailAddress.substring((maskLength + 1), emailAddress.length());
			email = emailAddress + "@" + emailDomain;
		}
		return email;
	}

}
