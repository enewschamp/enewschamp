package com.enewschamp.app.signin.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.otp.entity.OTP;
import com.enewschamp.app.otp.repository.OTPRepository;
import com.enewschamp.app.school.service.SchoolPricingService;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.app.user.login.entity.UserAction;
import com.enewschamp.app.user.login.entity.UserActivityTracker;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.domain.business.SchoolDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentPaymentBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionPeriodBusiness;
import com.enewschamp.subscription.domain.service.StudentDetailsWorkService;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.subscription.domain.service.StudentSchoolWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;
import com.enewschamp.subscription.pricing.service.IndividualPricingService;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "ResetPasswordPageHandler")
public class ResetPasswordPageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	UserService userService;

	@Autowired
	StudentRegistrationBusiness studentRegBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	OTPRepository otpRepository;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	PropertiesBackendService propertiesService;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SubscriptionPeriodBusiness subscriptionPeriodBusiness;

	@Autowired
	SchoolPricingService schoolPricingService;

	@Autowired
	StudentRegistrationService regService;

	@Autowired
	StudentPaymentWorkService studentPaymentWorkService;

	@Autowired
	StudentPaymentBusiness studentPaymentBusiness;

	@Autowired
	IndividualPricingService individualPricingService;

	@Autowired
	SchoolDetailsBusiness schoolDetailsBusiness;

	@Autowired
	StudentDetailsBusiness studentDetailsBusiness;

	@Autowired
	StudentSubscriptionWorkService studentSubscriptionWorkService;

	@Autowired
	StudentDetailsWorkService studentDetailsWorkService;

	@Autowired
	StudentSchoolWorkService studentSchoolWorkService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		String methodName = pageNavigationContext.getLoadMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[1];
			params[0] = PageNavigationContext.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			try {
				return (PageDTO) m.invoke(this, pageNavigationContext);
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
		return pageDTO;
	}

	public PageDTO loadResetPasswordPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		String module = pageNavigationContext.getPageRequest().getHeader().getModule();
		pageDto.setHeader(header);
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		ResetPasswordPageData resetPasswordPageData = new ResetPasswordPageData();
		List<OTP> otpGenList = otpRepository.getOtpGenListForEmail(emailId, RecordInUseType.Y);
		if (otpGenList != null && otpGenList.size() >= Integer
				.valueOf(propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
						PropertyConstants.OTP_GEN_MAX_ATTEMPTS))) {
			throw new BusinessException(ErrorCodeConstants.OTP_GEN_MAX_ATTEMPTS_EXHAUSTED);
		}
		studentRegBusiness.sendOtp(module, emailId);
		pageDto.setData(resetPasswordPageData);
		return pageDto;
	}

	public PageDTO loadSetPasswordPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);
		ResetPasswordPageData resetPasswordPageData = new ResetPasswordPageData();
		String module = pageNavigationContext.getPageRequest().getHeader().getModule();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		List<OTP> otpGenList = otpRepository.getOtpGenListForEmail(emailId, RecordInUseType.Y);
		if (otpGenList != null && otpGenList.size() >= Integer
				.valueOf(propertiesService.getValue(module, PropertyConstants.OTP_GEN_MAX_ATTEMPTS))) {
			throw new BusinessException(ErrorCodeConstants.OTP_GEN_MAX_ATTEMPTS_EXHAUSTED);
		}
		studentRegBusiness.sendOtp(module, emailId);
		pageDto.setData(resetPasswordPageData);
		return pageDto;
	}

	public PageDTO loadChangePasswordPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);
		ResetPasswordPageData resetPasswordPageData = new ResetPasswordPageData();
		pageDto.setData(resetPasswordPageData);
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
		return pageDTO;
	}

	public PageDTO handleSetPasswordAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		String action = pageRequest.getHeader().getAction();
		String emailId = pageRequest.getHeader().getEmailId();
		String module = pageRequest.getHeader().getModule();
		ResetPasswordPageData resetPasswordPageData = new ResetPasswordPageData();
		String password = "";
		String securityCode = null;
		String verifyPassword = "";
		String deviceId = "";
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("SYSTEM");
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.P);
		userActivityTracker.setActionTime(LocalDateTime.now());
		try {
			resetPasswordPageData = objectMapper.readValue(pageRequest.getData().toString(),
					ResetPasswordPageData.class);
			emailId = resetPasswordPageData.getEmailId();
			securityCode = resetPasswordPageData.getSecurityCode();
			password = resetPasswordPageData.getNewPassword();
			verifyPassword = resetPasswordPageData.getConfirmNewPassword();
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (password == null && "".equals(password)) {
			throw new BusinessException(ErrorCodeConstants.INVALID_PASSWORD);
			// throw exception..
		}
		if (verifyPassword == null && "".equals(verifyPassword)) {
			// throw excecption..
			throw new BusinessException(ErrorCodeConstants.INVALID_VERIFY_PWD);
		}
		if (emailId == null && "".equals(emailId)) {
			// throw exception..
			throw new BusinessException(ErrorCodeConstants.INVALID_EMAIL_ID);
		}

		if (securityCode == null) {
			// throw exception
			throw new BusinessException(ErrorCodeConstants.INVALID_SECURITY_CODE);
		}
		if (!password.equals(verifyPassword)) {
			// throw exception..
			throw new BusinessException(ErrorCodeConstants.PWD_VPWD_DONT_MATCH);
		}
		if (password.length() > 20 || password.length() < 8) {
			throw new BusinessException(ErrorCodeConstants.INVALID_PWD_LEN);
		}
		StudentRegistration student = regService.getStudentReg(emailId);
		if ("N".equals(student.getIsActive())) {
			throw new BusinessException(ErrorCodeConstants.STUD_IS_INACTIVE);
		}
		if (password.equals(student.getPassword()) || password.equals(student.getPassword1())
				|| password.equals(student.getPassword2())) {
			throw new BusinessException(ErrorCodeConstants.USER_PASSWORD_SAME_OR_PREV_TWO);
		}
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		userLoginBusiness.auditUserActivity(userActivityTracker);
		boolean validOtp = studentRegBusiness.validateOtp(module, securityCode, emailId);
		if (!validOtp) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.INVALID_SECURITY_CODE);
		}
		studentRegBusiness.resetPassword(emailId, password);
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		studentControlWorkDTO.setEmailIdVerified("Y");
		studentControlBusiness.saveAsWork(studentControlWorkDTO);
		resetPasswordPageData.setMessage(propertiesService.getValue(module, PropertyConstants.PASSWORD_RESET_MESSAGE));
		pageDto.setData(resetPasswordPageData);
		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	public PageDTO handleResetPasswordAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		String emailId = pageRequest.getHeader().getEmailId();
		String module = pageRequest.getHeader().getModule();
		ResetPasswordPageData resetPasswordPageData = new ResetPasswordPageData();
		String password = "";
		String securityCode = null;
		String verifyPassword = "";
		String deviceId = "";
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("SYSTEM");
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.P);
		userActivityTracker.setActionTime(LocalDateTime.now());
		try {
			resetPasswordPageData = objectMapper.readValue(pageRequest.getData().toString(),
					ResetPasswordPageData.class);
			emailId = resetPasswordPageData.getEmailId();
			securityCode = resetPasswordPageData.getSecurityCode();
			password = resetPasswordPageData.getNewPassword();
			verifyPassword = resetPasswordPageData.getConfirmNewPassword();
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (password == null && "".equals(password)) {
			throw new BusinessException(ErrorCodeConstants.INVALID_PASSWORD);
			// throw exception..
		}
		if (verifyPassword == null && "".equals(verifyPassword)) {
			throw new BusinessException(ErrorCodeConstants.INVALID_VERIFY_PWD);
		}
		if (emailId == null && "".equals(emailId)) {
			throw new BusinessException(ErrorCodeConstants.INVALID_EMAIL_ID);
		}
		if (securityCode == null) {
			throw new BusinessException(ErrorCodeConstants.INVALID_SECURITY_CODE);
		}
		if (!password.equals(verifyPassword)) {
			throw new BusinessException(ErrorCodeConstants.PWD_VPWD_DONT_MATCH);
		}
		if (password.length() > 20 || password.length() < 8) {
			throw new BusinessException(ErrorCodeConstants.INVALID_PWD_LEN);
		}
		StudentRegistration student = regService.getStudentReg(emailId);
		if ("N".equals(student.getIsActive())) {
			throw new BusinessException(ErrorCodeConstants.STUD_IS_INACTIVE);
		}
		if (password.equals(student.getPassword()) || password.equals(student.getPassword1())
				|| password.equals(student.getPassword2())) {
			throw new BusinessException(ErrorCodeConstants.USER_PASSWORD_SAME_OR_PREV_TWO);
		}
		boolean validOtp = studentRegBusiness.validateOtp(module, securityCode, emailId);
		if (!validOtp) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.INVALID_SECURITY_CODE);
		}
		studentRegBusiness.resetPassword(emailId, password);
		resetPasswordPageData.setMessage(propertiesService.getValue(module, PropertyConstants.PASSWORD_RESET_MESSAGE));
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		userLoginBusiness.auditUserActivity(userActivityTracker);
		pageDto.setData(resetPasswordPageData);
		return pageDto;
	}

	public PageDTO handleResendSecurityCodeAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		String emailId = pageRequest.getHeader().getEmailId();
		String module = pageRequest.getHeader().getModule();
		ResetPasswordPageData resetPasswordPageData = new ResetPasswordPageData();
		String deviceId = "";
		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("SYSTEM");
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.P);
		userActivityTracker.setActionTime(LocalDateTime.now());
		try {
			resetPasswordPageData = objectMapper.readValue(pageRequest.getData().toString(),
					ResetPasswordPageData.class);
			emailId = resetPasswordPageData.getEmailId();
			if (emailId == null && "".equals(emailId)) {
				// throw exception..
				throw new BusinessException(ErrorCodeConstants.INVALID_EMAIL_ID);
			}

		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		List<OTP> otpGenList = otpRepository.getOtpGenListForEmail(emailId, RecordInUseType.Y);
		if (otpGenList != null && otpGenList.size() >= Integer
				.valueOf(propertiesService.getValue(module, PropertyConstants.OTP_GEN_MAX_ATTEMPTS))) {
			throw new BusinessException(ErrorCodeConstants.OTP_GEN_MAX_ATTEMPTS_EXHAUSTED);
		}
		resetPasswordPageData.setMessage(propertiesService.getValue(module, PropertyConstants.OTP_MESSAGE));
		studentRegBusiness.sendOtp(module, emailId);
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		userLoginBusiness.auditUserActivity(userActivityTracker);
		pageDto.setData(resetPasswordPageData);
		return pageDto;
	}

	public PageDTO handleChangePasswordAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		String tokenId = pageRequest.getHeader().getLoginCredentials();
		String emailId = pageRequest.getHeader().getEmailId();
		String deviceId = pageRequest.getHeader().getDeviceId();
		String module = pageRequest.getHeader().getModule();
		ResetPasswordPageData resetPasswordPageData = new ResetPasswordPageData();

		UserActivityTracker userActivityTracker = new UserActivityTracker();
		userActivityTracker.setOperatorId("SYSTEM");
		userActivityTracker.setRecordInUse(RecordInUseType.Y);
		userActivityTracker.setActionPerformed(action);
		userActivityTracker.setDeviceId(deviceId);
		userActivityTracker.setUserId(emailId);
		userActivityTracker.setUserType(UserType.P);
		userActivityTracker.setActionTime(LocalDateTime.now());
		String password = "";
		String passwordNew = "";
		String passwordRepeat = "";
		try {
			resetPasswordPageData = objectMapper.readValue(pageRequest.getData().toString(),
					ResetPasswordPageData.class);
			password = resetPasswordPageData.getCurrentPassword();
			passwordNew = resetPasswordPageData.getNewPassword();
			passwordRepeat = resetPasswordPageData.getConfirmNewPassword();
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (password == null || "".equals(password)) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.INVALID_USER_PASSWORD);
		} else if (passwordNew == null || "".equals(passwordNew)) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.INVALID_USER_NEW_PASSWORD);
		} else if (passwordRepeat == null || "".equals(passwordRepeat)) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.INVALID_USER_NEW_PASSWORD);
		} else if (!passwordNew.equals(passwordRepeat)) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.BOTH_PASSWORD_DO_NOT_MATCH);
		} else if (passwordNew.equals(password)) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.OLD_NEW_PASSWORD_SAME);
		}
		boolean validPassword = studentRegBusiness.validatePassword(module, emailId, password, deviceId, tokenId);
		if (!validPassword) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.INVALID_USERNAME_OR_PASSWORD, emailId);
		}
		StudentRegistration student = regService.getStudentReg(emailId);
		if ("N".equals(student.getIsActive())) {
			throw new BusinessException(ErrorCodeConstants.STUD_IS_INACTIVE);
		}
		if (passwordNew.equals(student.getPassword()) || passwordNew.equals(student.getPassword1())
				|| passwordNew.equals(student.getPassword2())) {
			throw new BusinessException(ErrorCodeConstants.USER_PASSWORD_SAME_OR_PREV_TWO);
		}
		studentRegBusiness.changePassword(emailId, passwordRepeat, userActivityTracker);
		userActivityTracker.setActionStatus(UserAction.SUCCESS);
		userLoginBusiness.auditUserActivity(userActivityTracker);
		resetPasswordPageData = new ResetPasswordPageData();
		resetPasswordPageData.setMessage("Password Changed Successfully");
		pageDto.setData(resetPasswordPageData);
		return pageDto;
	}

}
