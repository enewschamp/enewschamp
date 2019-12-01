package com.enewschamp.app.signin.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationErrorProperties;
import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "SignInPageHandler")
public class SignInPageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentRegistrationBusiness studentRegBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	EnewschampApplicationProperties appConfig;

	@Autowired
	EnewschampApplicationErrorProperties errorProperties;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);
		SignInPageData signInPageData = new SignInPageData();
		signInPageData.setContactUsText("Contact Us : " + appConfig.getHelpDeskEmail());
		pageDto.setData(signInPageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String action = pageRequest.getHeader().getAction();
		SignInPageData signInPageData = new SignInPageData();
		String emailId = "";
		String password = "";
		Long securityCode = null;
		String verifyPassword = "";
		String deviceId = "";

		if (PageAction.CreateAccount.toString().equalsIgnoreCase(actionName)) {
			pageDto.setData(signInPageData);
		} else if (PageAction.SecurityCode.toString().equalsIgnoreCase(action)) {
			// String emailId = pageRequest.getData().
			try {
				signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
				emailId = signInPageData.getEmailId();

			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			studentRegBusiness.sendOtp(emailId);
			signInPageData.setMessage(appConfig.getOtpMessage());

			pageDto.setData(signInPageData);
		} else if (PageAction.register.toString().equalsIgnoreCase(action)) {
			boolean optValidation = false;

			try {
				signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
				emailId = signInPageData.getEmailId();
				securityCode = signInPageData.getSecurityCode();
				password = signInPageData.getPassword();
				verifyPassword = signInPageData.getVerifyPassword();

			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (password == null && "".equals(password)) {
				throw new BusinessException(ErrorCodes.INVALID_PASSWORD, "Invalid Password");
				// throw exception..
			}
			if (verifyPassword == null && "".equals(verifyPassword)) {
				// throw excecption..
				throw new BusinessException(ErrorCodes.INVALID_VERIFY_PWD, "Invalid Verify Password");
			}
			if (emailId == null && "".equals(emailId)) {
				// throw exception..
				throw new BusinessException(ErrorCodes.INVALID_EMAIL_ID, "Invalid Email Id");
			}

			if (securityCode == null) {
				// throw exception
				throw new BusinessException(ErrorCodes.INVALID_SECURITY_CODE, "Invalid Security Code");
			}
			if (!password.equals(verifyPassword)) {
				// throw exception..
				throw new BusinessException(ErrorCodes.PWD_VPWD_DONT_MATCH,
						"Password and Verify Password do not match");
			}

			optValidation = studentRegBusiness.validateOtp(emailId, securityCode);
			if (optValidation) {
				studentRegBusiness.register(emailId, password);

			} else {
				// throw exception...
				throw new BusinessException(ErrorCodes.SEC_CODE_VALIDATION_FAILURE, "Incorrect Security Code");
			}
			signInPageData.setMessage(appConfig.getRegistrationMessage());

			pageDto.setData(signInPageData);

		} else if (PageAction.DeleteAccount.toString().equalsIgnoreCase(action)) {
			try {
				signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
				emailId = signInPageData.getEmailId();

			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			studentRegBusiness.deleteAccount(emailId);
			signInPageData.setMessage(appConfig.getAccountDeletionMessage());

			pageDto.setData(signInPageData);

		} else if (PageAction.ResetPassword.toString().equalsIgnoreCase(action)) {
			try {
				signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
				emailId = signInPageData.getEmailId();
				securityCode = signInPageData.getSecurityCode();
				password = signInPageData.getPassword();
				verifyPassword = signInPageData.getVerifyPassword();

			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (password == null && "".equals(password)) {
				throw new BusinessException(ErrorCodes.INVALID_PASSWORD, "Invalid Password");
				// throw exception..
			}
			if (verifyPassword == null && "".equals(verifyPassword)) {
				// throw excecption..
				throw new BusinessException(ErrorCodes.INVALID_VERIFY_PWD, "Invalid Verify Password");
			}
			if (emailId == null && "".equals(emailId)) {
				// throw exception..
				throw new BusinessException(ErrorCodes.INVALID_EMAIL_ID, "Invalid Email Id");
			}

			if (securityCode == null) {
				// throw exception
				throw new BusinessException(ErrorCodes.INVALID_SECURITY_CODE, "Invalid Security Code");
			}
			if (!password.equals(verifyPassword)) {
				// throw exception..
				throw new BusinessException(ErrorCodes.PWD_VPWD_DONT_MATCH,
						"Password and Verify Password do not match");
			}
			signInPageData.setMessage(appConfig.getPwdResetMessage());

			pageDto.setData(signInPageData);

			studentRegBusiness.resetPassword(emailId, password);
		} else if (PageAction.ResendSecurityCode.toString().equalsIgnoreCase(action)) {
			try {
				signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
				emailId = signInPageData.getEmailId();
				if (emailId == null && "".equals(emailId)) {
					// throw exception..
					throw new BusinessException(ErrorCodes.INVALID_EMAIL_ID, "Invalid Email Id");
				}

			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			signInPageData.setMessage(appConfig.getOtpMessage());

			studentRegBusiness.sendOtp(emailId);

		} else if (PageAction.LoginStudent.toString().equalsIgnoreCase(action)) {
			boolean loginSuccess = false;
			try {
				signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
				emailId = signInPageData.getEmailId();
				password = signInPageData.getPassword();
				deviceId = pageRequest.getHeader().getDeviceId();
			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			loginSuccess = studentRegBusiness.validatePassword(emailId, password);
			if (loginSuccess) {
				userLoginBusiness.login(emailId, deviceId, UserType.S);
			} else {
				throw new BusinessException(ErrorCodes.INVALID_EMAILID_OR_PASSWORD, "Invalid User Id Or Password");
			}
			pageDto.setData(signInPageData);
		} else if (PageAction.logout.toString().equalsIgnoreCase(action)) {
			try {
				signInPageData = objectMapper.readValue(pageRequest.getData().toString(), SignInPageData.class);
				emailId = signInPageData.getEmailId();
				deviceId = pageRequest.getHeader().getDeviceId();
			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			userLoginBusiness.logout(emailId, deviceId, UserType.S);
			pageDto.setData(signInPageData);

		}
		return pageDto;
	}

}
