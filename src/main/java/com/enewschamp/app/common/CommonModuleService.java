package com.enewschamp.app.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.enewschamp.app.signin.page.handler.LoginPageData;
import com.enewschamp.app.user.login.entity.UserAction;
import com.enewschamp.app.user.login.entity.UserActivityTracker;
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.app.user.login.service.UserLoginService;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.user.domain.entity.User;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommonModuleService {
	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	UserService userService;

	@Autowired
	EditionService editionService;
	
	@Autowired
	private PropertiesService propertiesService;
	
	@Autowired
	private UserLoginService userLoginService;
	private Validator validator;
	
	public PageDTO performRefreshToken(PageRequestDTO pageRequest, String loginCredentials, String userId,
			String deviceId, UserActivityTracker userActivityTracker, UserType userType) {
		PageDTO pageResponse;
		String edition = pageRequest.getHeader().getEditionId();
		editionService.getEdition(edition);
		userLoginBusiness.isUserLoggedIn(deviceId, loginCredentials, userId, userType, userActivityTracker);
		UserLogin userLogin = userLoginBusiness.login(userId, deviceId, loginCredentials, userType);
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		userLoginBusiness.auditUserActivity(userActivityTracker);
		pageResponse = new PageDTO();
		pageRequest.getHeader().setRequestStatus(RequestStatusType.S);
		pageResponse.setHeader(pageRequest.getHeader());
		LoginPageData loginPageData = new LoginPageData();
		loginPageData.setMessage("Token Refreshed Successfully");
		loginPageData.setLoginCredentials(userLogin.getTokenId());
		loginPageData.setTokenValidity(
				propertiesService.getProperty(PropertyConstants.PUBLISHER_SESSION_EXPIRY_SECS));
		pageResponse.setData(loginPageData);
		pageResponse.getHeader().setLoginCredentials(null);
		return pageResponse;
	}

	public UserActivityTracker validateUser(PageRequestDTO pageRequest, String module, String pageName,
			String actionName, String loginCredentials, String userId, String deviceId, String operation,
			String editionId, UserType userType, String moduleName) {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null));
		User user = userService.get(userId);
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("SYSTEM");
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(actionName);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(userId);
		userActivityTracker.setUserType(userType);
		userActivityTracker.setActionTime(LocalDateTime.now());
		if (user == null) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.INVALID_USER_ID, userId);
		}
		if (!user.getIsActive().equalsIgnoreCase(RecordInUseType.Y.toString())) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.USER_IS_INACTIVE, userId);
		}
		// Check if user has been logged in
		if (!(pageName.equalsIgnoreCase("Login"))) {
			userLoginBusiness.isUserLoggedIn(deviceId, loginCredentials, userId, userType);
			JsonNode dataNode = pageRequest.getData();
			if (dataNode != null && dataNode instanceof ObjectNode) {
				((ObjectNode) dataNode).put("operatorId", userId);
			}
		}
		return userActivityTracker;
	}
	
	public LoginPageData getLoginPageData(String userId, UserType userType) {
		User user = userService.get(userId);
		LoginPageData loginPageData = new LoginPageData();
		if (user != null) {
			loginPageData.setUserName(user.getName() + " " + user.getSurname());
			loginPageData.setTodaysDate(LocalDate.now());
			loginPageData.setTheme(user.getTheme());
			loginPageData.setUserRole(userLoginBusiness.getUserRole(userId));
			loginPageData.setLoginCredentials(userLoginService.getOperatorLogin(userId, userType).getTokenId());
			loginPageData
					.setTokenValidity(propertiesService.getProperty(PropertyConstants.PUBLISHER_SESSION_EXPIRY_SECS));
		}
		return loginPageData;
	}
	
	public void validateHeaders(HeaderDTO pageData, String module) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<HeaderDTO>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
		}
		
		if ((!propertiesService.getProperty(PropertyConstants.ADMIN_MODULE_NAME).equals(module))) {
			log.error("Module name doesn't match");
			throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
		}
	}

}
