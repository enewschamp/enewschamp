package com.enewschamp.app.signin.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
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
	EnewschampApplicationProperties appConfig;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		// pageDto.setData(pageNavigationContext.getPageRequest().getData());
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String action = pageNavigationContext.getPageRequest().getHeader().getAction();
		SignInPageData signInPageData = new SignInPageData();

		signInPageData = modelMapper.map(pageNavigationContext.getPreviousPageResponse().getData(),
				SignInPageData.class);

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

		if (PageAction.SecurityCode.toString().equalsIgnoreCase(action)) {
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
		}
		if (PageAction.register.toString().equalsIgnoreCase(action)) {
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

		}
		if (PageAction.DeleteAccount.toString().equalsIgnoreCase(action)) {
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

		}
		if (PageAction.ResetPassword.toString().equalsIgnoreCase(action)) {
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
		}
		if (PageAction.ResendSecurityCode.toString().equalsIgnoreCase(action)) {

			// String emailId = pageRequest.getData().
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

		}

		return pageDto;
	}

}
