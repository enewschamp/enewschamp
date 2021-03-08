package com.enewschamp.app.user.login.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.user.login.entity.UserAction;
import com.enewschamp.app.user.login.entity.UserActivityTracker;
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.repository.UserActivityTrackerRepository;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.entity.UserRole;
import com.enewschamp.user.domain.service.UserRoleService;

@Service
public class UserLoginBusiness {

	@Autowired
	UserLoginService loginService;

	@Autowired
	UserRoleService userRoleService;

	@Autowired
	UserActivityTrackerRepository userActivityTrackerRepository;

	@Autowired
	private PropertiesBackendService propertiesService;

	public UserLogin newDeviceLogin(final String userId, final String deviceId, UserType userType) {
		UserLogin userLogin = getStudentLoginByDeviceId(deviceId);
		if (userLogin == null) {
			userLogin = new UserLogin();
		}
		userLogin.setUserId(userId);
		userLogin.setDeviceId(deviceId);
		userLogin.setLoginFlag(AppConstants.NO);
		userLogin.setLastLoginTime(LocalDateTime.now());
		userLogin.setTokenExpirationTime(LocalDateTime.now().plusSeconds(
				Integer.valueOf(propertiesService.getValue("Common", PropertyConstants.LOGIN_SESSION_EXPIRY_SECS))));
		userLogin.setTokenId("" + System.currentTimeMillis());
		userLogin.setOperatorId(userId);
		userLogin.setUserType(userType);
		userLogin.setRecordInUse(RecordInUseType.Y);
		return loginService.create(userLogin);
	}

	public UserLogin login(final String userId, final String deviceId, final String tokenId, final String appName,
			final String appVersion, UserType userType) {
		UserLogin loggedIn = getUserLoginInstance(userId, deviceId, tokenId, userType);
		if (loggedIn == null) {
			loggedIn = loginService.getOperatorLogin(userId);
		}
		if (UserType.S.equals(userType) && loggedIn == null) {
			loggedIn = getStudentLoginByDeviceId(deviceId);
		}
		if (loggedIn == null) {
			UserLogin userLogin = new UserLogin();
			userLogin.setUserId((userId == null ? "unknown" : userId));
			userLogin.setDeviceId(deviceId);
			userLogin.setLoginFlag(AppConstants.NO);
			userLogin.setLastLoginTime(LocalDateTime.now());
			userLogin.setTokenExpirationTime(LocalDateTime.now().plusSeconds(Integer
					.valueOf(propertiesService.getValue("Common", PropertyConstants.LOGIN_SESSION_EXPIRY_SECS))));
			userLogin.setTokenId("" + System.currentTimeMillis());
			userLogin.setAppName(appName);
			userLogin.setAppVersion(appVersion);
			userLogin.setOperatorId((userId == null ? "unknown" : userId));
			userLogin.setUserType(userType);
			userLogin.setRecordInUse(RecordInUseType.Y);
			loggedIn = loginService.create(userLogin);
		} else {
			loggedIn.setLastLoginTime(LocalDateTime.now());
			loggedIn.setTokenExpirationTime(LocalDateTime.now().plusSeconds(Integer
					.valueOf(propertiesService.getValue("Common", PropertyConstants.LOGIN_SESSION_EXPIRY_SECS))));
			if (!loggedIn.getUserId().equalsIgnoreCase(userId)) {
				loggedIn.setUserId("");
				loggedIn.setLoginFlag(AppConstants.NO);
			}
			loggedIn.setTokenId("" + System.currentTimeMillis());
			loggedIn.setAppName(appName);
			loggedIn.setAppVersion(appVersion);
			loginService.update(loggedIn);
		}
		return loggedIn;
	}

	public UserLogin publisherLogin(final String userId, final String deviceId, final String tokenId,
			final String appName, final String appVersion, UserType userType) {
		UserLogin loggedIn = getUserLoginInstance(userId, deviceId, tokenId, userType);
		if (loggedIn == null) {
			loggedIn = loginService.getOperatorLogin(userId);
		}
		if (loggedIn == null) {
			UserLogin userLogin = new UserLogin();
			userLogin.setUserId(userId);
			userLogin.setDeviceId(deviceId);
			userLogin.setLoginFlag(AppConstants.YES);
			userLogin.setLastLoginTime(LocalDateTime.now());
			userLogin.setTokenExpirationTime(LocalDateTime.now().plusSeconds(Integer
					.valueOf(propertiesService.getValue("Common", PropertyConstants.LOGIN_SESSION_EXPIRY_SECS))));
			userLogin.setTokenId("" + System.currentTimeMillis());
			userLogin.setAppName(appName);
			userLogin.setAppVersion(appVersion);
			userLogin.setOperatorId(userId);
			userLogin.setUserType(userType);
			userLogin.setRecordInUse(RecordInUseType.Y);
			loggedIn = loginService.create(userLogin);
		} else {
			loggedIn.setLastLoginTime(LocalDateTime.now());
			loggedIn.setTokenExpirationTime(LocalDateTime.now().plusSeconds(Integer
					.valueOf(propertiesService.getValue("Common", PropertyConstants.LOGIN_SESSION_EXPIRY_SECS))));
			if (!userId.equalsIgnoreCase(loggedIn.getUserId())) {
				loggedIn.setUserId("");
				loggedIn.setLoginFlag(AppConstants.NO);
			} else {
				loggedIn.setLoginFlag(AppConstants.YES);
			}
			loggedIn.setTokenId("" + System.currentTimeMillis());
			loggedIn.setAppName(appName);
			loggedIn.setAppVersion(appVersion);
			loginService.update(loggedIn);
		}
		return loggedIn;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void auditUserActivity(UserActivityTracker userActivityTracker) {
		userActivityTrackerRepository.save(userActivityTracker);
	}

	public String getUserRole(final String userId) {
		UserRole userRole = userRoleService.getByUserId(userId);
		if (userRole != null) {
			return userRole.getUserRoleKey().getRoleId();
		} else {
			return "";
		}
	}

	public void logout(final String userId, final String deviceId, final String tokenId, UserType userType) {
		UserLogin loggedIn = getUserLoginInstance(userId, deviceId, tokenId, userType);
		if (loggedIn != null) {
			loggedIn.setLoginFlag(AppConstants.NO);
			loginService.update(loggedIn);
		}
	}

	public UserLogin getLoginDetails(final String deviceId, final String tokenId, final String userId,
			UserType userType) {
		UserLogin loggedIn = getUserLoginInstance(userId, deviceId, tokenId, userType);
		return loggedIn;

	}

	public UserLogin getNonLoginDetails(final String deviceId, final String tokenId, final String userId,
			UserType userType) {
		UserLogin loggedIn = loginService.getUserNonLogin(userId, deviceId, tokenId, userType);
		return loggedIn;
	}

	public boolean isUserLoggedIn(final String deviceId, final String tokenId, final String userId, UserType userType) {
		boolean isLoggedIn = true;
		UserLogin loggedIn = getUserLoginInstance(userId, deviceId, tokenId, userType);
		if (loggedIn != null) {
			if (LocalDateTime.now().isAfter(loggedIn.getTokenExpirationTime())) {
				loggedIn.setLoginFlag("N");
				loginService.repository.save(loggedIn);
				throw new BusinessException(ErrorCodeConstants.USER_TOKEN_EXPIRED);
			} else if (AppConstants.NO.toString().equals(loggedIn.getLoginFlag())) {
				throw new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS);
			}
		} else {
			throw new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS);
		}
		return isLoggedIn;
	}

	public boolean isUserLoggedIn(final String deviceId, final String tokenId, final String userId, UserType userType,
			UserActivityTracker userActivityTracker) {
		boolean isLoggedIn = false;
		UserLogin loggedIn = getUserLoginInstance(userId, deviceId, tokenId, userType);
		if (loggedIn != null) {
			if (LocalDateTime.now().isAfter(loggedIn.getTokenExpirationTime())) {
				loggedIn.setLoginFlag("N");
				loginService.repository.save(loggedIn);
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.USER_TOKEN_EXPIRED);
			} else if (AppConstants.NO.toString().equals(loggedIn.getLoginFlag())) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS);
			}
		} else {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS);
		}
		return isLoggedIn;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean isUserSessionValid(final String deviceId, final String tokenId, final String userId,
			UserType userType) {
		boolean isUserSessionValid = false;
		UserLogin loggedIn = getUserLoginInstance(userId, deviceId, tokenId, userType);
		if (loggedIn != null) {
			if (LocalDateTime.now().isAfter(loggedIn.getTokenExpirationTime())) {
				loggedIn.setLoginFlag("N");
				loginService.repository.save(loggedIn);
			} else {
				isUserSessionValid = true;
			}
		}
		return isUserSessionValid;
	}

	private UserLogin getUserLoginInstance(final String userId, final String deviceId, final String tokenId,
			UserType userType) {
		UserLogin loggedIn = null;
		if (UserType.S.toString().equals(userType.toString())) {
			loggedIn = loginService.getUserLogin(userId, deviceId, tokenId, userType);
		} else {
			loggedIn = loginService.getOperatorLogin(userId, deviceId, tokenId);
		}
		return loggedIn;
	}

	public UserLogin getStudentLoginByDeviceId(final String deviceId) {
		Optional<UserLogin> existingEntity = loginService.repository.getStudentLoginByDeviceId(deviceId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public UserLogin getDeviceLogin(final String deviceId, final String tokenId) {
		Optional<UserLogin> existingEntity = loginService.repository.getDeviceLogin(deviceId, tokenId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

}
