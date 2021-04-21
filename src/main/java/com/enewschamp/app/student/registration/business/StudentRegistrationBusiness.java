package com.enewschamp.app.student.registration.business;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.otp.entity.OTP;
import com.enewschamp.app.otp.repository.OTPRepository;
import com.enewschamp.app.otp.service.OTPService;
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
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;
import com.enewschamp.subscription.domain.service.StudentControlService;
import com.enewschamp.subscription.domain.service.StudentControlWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;

@Service
public class StudentRegistrationBusiness {

	@Autowired
	StudentRegistrationService regService;

	@Autowired
	ErrorCodesService errorCodesService;

	@Autowired
	UserLoginService loginService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	OTPService otpService;

	@Autowired
	OTPRepository otpRepository;

	@Autowired
	StudentSubscriptionService studentSubscriptionService;

	@Autowired
	StudentSubscriptionWorkService studentSubscriptionWorkService;

	@Autowired
	StudentControlService studentControlService;

	@Autowired
	StudentControlWorkService studentControlWorkService;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	PropertiesBackendService propertiesService;

	public boolean validatePassword(final String appName, final String emailId, final String password,
			final String deviceId, final String tokenId, UserActivityTracker userActivityTracker) {
		StudentRegistration student = regService.getStudentReg(emailId);
		if (student == null) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userActivityTracker.setErrorCode(ErrorCodeConstants.INVALID_USER_ID);
			userActivityTracker.setErrorDescription(errorCodesService.getValue(ErrorCodeConstants.INVALID_USER_ID));
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.INVALID_USER_ID, emailId);
		}
		if ("Y".equals(student.getIsDeleted())) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userActivityTracker.setErrorCode(ErrorCodeConstants.INVALID_EMAILID_OR_PASSWORD);
			userActivityTracker
					.setErrorDescription(errorCodesService.getValue(ErrorCodeConstants.INVALID_EMAILID_OR_PASSWORD));
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.INVALID_EMAILID_OR_PASSWORD);
		}
		if ("N".equals(student.getIsActive())) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userActivityTracker.setErrorCode(ErrorCodeConstants.STUD_IS_INACTIVE);
			userActivityTracker.setErrorDescription(errorCodesService.getValue(ErrorCodeConstants.STUD_IS_INACTIVE));
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.STUD_IS_INACTIVE);
		}
		if ("Y".equals(student.getIsAccountLocked()) && (student.getLastUnsuccessfulLoginAttempt() != null)) {
			LocalDate lastAttemptDate = student.getLastUnsuccessfulLoginAttempt().toLocalDate();
			LocalDate todayDate = LocalDateTime.now().toLocalDate();
			if (lastAttemptDate.isBefore(todayDate)) {
				student.setIsAccountLocked("N");
				student.setIncorrectLoginAttempts(0);
			}
		}
		if ("Y".equals(student.getIsAccountLocked())) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userActivityTracker.setErrorCode(ErrorCodeConstants.USER_ACCOUNT_LOCKED);
			userActivityTracker.setErrorDescription(errorCodesService.getValue(ErrorCodeConstants.USER_ACCOUNT_LOCKED));
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.USER_ACCOUNT_LOCKED, emailId);
		}
		boolean isValid = false;
		String studPass = student.getPassword();
		if (!studPass.equals(password)) {
			isValid = false;
			student.setLastUnsuccessfulLoginAttempt(LocalDateTime.now());
			student.setIncorrectLoginAttempts(student.getIncorrectLoginAttempts() + 1);
			boolean lockFlag = false;
			if (student.getIncorrectLoginAttempts() == Integer
					.valueOf(propertiesService.getValue(appName, PropertyConstants.LOGIN_MAX_ATTEMPTS))) {
				student.setIsAccountLocked("Y");
				lockFlag = true;
				UserLogin userLogin = loginService.getDeviceLogin(emailId, deviceId, tokenId, UserType.S);
				if (userLogin != null) {
					userLogin.setLoginFlag(AppConstants.NO);
					loginService.update(userLogin);
				}
			}
			regService.update(student);
			if (lockFlag) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userActivityTracker.setErrorCode(ErrorCodeConstants.USER_ACCOUNT_GET_LOCKED);
				userActivityTracker
						.setErrorDescription(errorCodesService.getValue(ErrorCodeConstants.USER_ACCOUNT_GET_LOCKED));
				userLoginBusiness.auditUserActivity(userActivityTracker);
				throw new BusinessException(ErrorCodeConstants.USER_ACCOUNT_GET_LOCKED, emailId);
			}
		} else {
			isValid = true;
			student.setLastSuccessfulLoginAttempt(LocalDateTime.now());
			student.setLastUnsuccessfulLoginAttempt(null);
			student.setIncorrectLoginAttempts(0);
			regService.update(student);
		}
		return isValid;
	}

	public void deleteAccount(final String emailId) {
		StudentRegistration student = regService.getStudentReg(emailId);
		if ("Y".equals(student.getIsDeleted())) {
			throw new BusinessException(ErrorCodeConstants.STUD_ACCOUNT_DELETED);
		}
		if ("N".equals(student.getIsActive())) {
			throw new BusinessException(ErrorCodeConstants.STUD_IS_INACTIVE);
		}
		student.setIsDeleted("Y");
		regService.update(student);
	}

	public void changePassword(final String emailId, final String password, UserActivityTracker userActivityTracker) {
		StudentRegistration student = regService.getStudentReg(emailId);
		if (!student.getRecordInUse().equals(RecordInUseType.Y)) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.USER_IS_INACTIVE, emailId);
		}
		if (password.equals(student.getPassword()) || password.equals(student.getPassword1())
				|| password.equals(student.getPassword2())) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.USER_PASSWORD_SAME_OR_PREV_TWO, emailId);
		}
		student.setPassword2(student.getPassword1());
		student.setPassword1(student.getPassword());
		student.setForcePasswordChange("N");
		student.setPassword(password);
		regService.update(student);
	}

	public void resetPassword(final String emailId, final String password, UserActivityTracker userActivityTracker) {
		if (password.length() > 20 || password.length() < 8) {
			userActivityTracker.setErrorCode(ErrorCodeConstants.INVALID_SECURITY_CODE);
			userActivityTracker
					.setErrorDescription(errorCodesService.getValue(ErrorCodeConstants.INVALID_SECURITY_CODE));
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.INVALID_PWD_LEN);
		}
		StudentRegistration student = regService.getStudentReg(emailId);
		if ("N".equals(student.getIsActive())) {
			userActivityTracker.setErrorCode(ErrorCodeConstants.INVALID_SECURITY_CODE);
			userActivityTracker
					.setErrorDescription(errorCodesService.getValue(ErrorCodeConstants.INVALID_SECURITY_CODE));
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.STUD_IS_INACTIVE);
		}
		if (password.equals(student.getPassword()) || password.equals(student.getPassword1())
				|| password.equals(student.getPassword2())) {
			userActivityTracker.setErrorCode(ErrorCodeConstants.INVALID_SECURITY_CODE);
			userActivityTracker
					.setErrorDescription(errorCodesService.getValue(ErrorCodeConstants.INVALID_SECURITY_CODE));
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.USER_PASSWORD_SAME_OR_PREV_TWO);
		}
		student.setPassword2(student.getPassword1());
		student.setPassword1(student.getPassword());
		student.setPassword(password);
		student.setIncorrectLoginAttempts(0);
		student.setIsAccountLocked("N");
		student.setForcePasswordChange("N");
		regService.update(student);
	}

	public void sendOtp(final String appName, final String emailId) {
		StudentRegistration student = regService.getStudentReg(emailId);
		Long studentId = 0L;
		if (student == null) {
			throw new BusinessException(ErrorCodeConstants.STUD_REG_NOT_FOUND, emailId);
		} else {
			studentId = student.getStudentId();
		}
		if ("N".equals(student.getIsActive())) {
			throw new BusinessException(ErrorCodeConstants.STUD_IS_INACTIVE, emailId);
		}
		List<OTP> otpGenList = otpRepository.getOtpGenListForEmail(emailId, RecordInUseType.Y);
		if (otpGenList != null && otpGenList.size() > Integer
				.valueOf(propertiesService.getValue(appName, PropertyConstants.OTP_GEN_MAX_ATTEMPTS))) {
			throw new BusinessException(ErrorCodeConstants.OTP_GEN_MAX_ATTEMPTS_EXHAUSTED, emailId);
		} else {
			otpService.genOTP(appName, "" + studentId, emailId, null);
		}

	}

	public boolean validateOtp(final String appName, final String otp, final String emailId,
			UserActivityTracker userActivityTracker) {
		return otpService.validateOtp(appName, otp, emailId, userActivityTracker);
	}

	public void checkAndUpdateIfSubscriptionExpired(String emailId, String editionId) {
		StudentControl studentControl = studentControlService.getStudentByEmail(emailId);
		if (studentControl != null) {
			StudentSubscription studentSubscription = studentSubscriptionService.get(studentControl.getStudentId(),
					editionId);
			if (!"F".equalsIgnoreCase(studentSubscription.getSubscriptionSelected())) {
				if (studentSubscription.getEndDate().isBefore(LocalDate.now())) {
					studentSubscription.setSubscriptionSelected("F");
					studentSubscriptionService.update(studentSubscription);
					studentControl.setSubscriptionType("F");
					studentControlService.update(studentControl);
					StudentControlWork studentEntityWork = studentControlWorkService.getStudentByEmail(emailId);
					if (studentEntityWork != null) {
						studentEntityWork.setSubscriptionType("F");
						studentControlWorkService.update(studentEntityWork);
					}
				}
			}
		}
	}

	public void checkAndUpdateIfEvalPeriodExpired(String emailId, String editionId) {
		StudentControl studentEntity = studentControlService.getStudentByEmail(emailId);
		if (studentEntity != null && !"Y".equals(studentEntity.getEvalAvailed())) {
			StudentSubscription studentSubscription = studentSubscriptionService.get(studentEntity.getStudentId(),
					editionId);
			if (studentSubscription != null && "F".equalsIgnoreCase(studentSubscription.getSubscriptionSelected())
					&& studentSubscription.getEndDate().isBefore(LocalDate.now())) {
				studentEntity.setEvalAvailed("Y");
				studentControlService.update(studentEntity);
				StudentControlWork studentEntityWork = studentControlWorkService.getStudentByEmail(emailId);
				StudentSubscriptionWork studentSubscriptionWork = studentSubscriptionWorkService
						.get(studentEntity.getStudentId(), editionId);
				if (studentSubscriptionWork != null
						&& "F".equalsIgnoreCase(studentSubscriptionWork.getSubscriptionSelected())) {
					studentEntityWork.setEvalAvailed("Y");
					studentControlWorkService.update(studentEntityWork);
				}
			}
		}
	}

}