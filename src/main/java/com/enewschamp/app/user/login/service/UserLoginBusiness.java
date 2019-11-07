package com.enewschamp.app.user.login.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginService;
import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.common.RecordInUseType;

@Service
public class UserLoginBusiness {

	@Autowired
	UserLoginService loginService;

	public UserLogin login(final String userId, final String deviceId, UserType userType) {
		UserLogin loggedIn = getUserLoginInstance(userId, deviceId, userType);
		if (loggedIn == null) {
			UserLogin userLogin = new UserLogin();
			userLogin.setUserId(userId);
			userLogin.setDeviceId(deviceId);
			userLogin.setLoginFlag(AppConstants.YES);
			userLogin.setLastLoginTime(LocalDateTime.now());
			userLogin.setOperatorId("SYSTEM");
			userLogin.setUserType(userType);
			userLogin.setRecordInUse(RecordInUseType.Y);
			loggedIn = loginService.create(userLogin);
		} else {
			loggedIn.setLastLoginTime(LocalDateTime.now());
			loggedIn.setLoginFlag(AppConstants.YES);
			loginService.update(loggedIn);
		}
		return loggedIn;
	}

	public void logout(final String userId, final String deviceId, UserType userType) {
		UserLogin loggedIn = getUserLoginInstance(userId, deviceId, userType);
		if (loggedIn != null) {
			loggedIn.setLoginFlag(AppConstants.NO);
			loginService.update(loggedIn);
		}
	}
	
	public UserLogin getLoginDetails(final String deviceId, final String userId, UserType userType) {
		UserLogin loggedIn = getUserLoginInstance(userId, deviceId, userType);
		return loggedIn;

	}

	public boolean isUserLoggedIn(final String deviceId, final String userId, UserType userType) {
		boolean isLoggedIn = false;
		UserLogin loggedIn = getUserLoginInstance(userId, deviceId, userType);
		if (loggedIn != null) {
			if (AppConstants.YES.toString().equals(loggedIn.getLoginFlag())) {
				isLoggedIn = true;
			} else {
				isLoggedIn = false;
			}
		}
		return isLoggedIn;
	}

	private UserLogin getUserLoginInstance(final String userId, final String deviceId, UserType userType) {
		UserLogin loggedIn = null;
		if (userType.equals(UserType.S)) {
			loggedIn = loginService.getUserLogin(userId, deviceId, userType);
		} else {
			loggedIn = loginService.getOperatorLogin(userId, deviceId);
		}
		return loggedIn;
	}
}
