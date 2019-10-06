package com.enewschamp.app.student.login.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.student.login.entity.StudentLogin;
import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.common.RecordInUseType;

@Service
public class StudentLoginBusiness {
	
	
	@Autowired
	StudentLoginService loginService;
	
	public StudentLogin login(final String emailId, final String deviceId)
	{
		StudentLogin loggedIn = loginService.getStudenLogin(emailId, deviceId);
		if(loggedIn==null)
		{
			StudentLogin studentLogin = new StudentLogin();
			studentLogin.setEmailId(emailId);
			studentLogin.setDeviceId(deviceId);
			studentLogin.setLoginFlag(AppConstants.YES);
			studentLogin.setLastLoginTime(LocalDateTime.now());
			studentLogin.setOperatorId("SYSTEM");
			studentLogin.setRecordInUse(RecordInUseType.Y);
			loggedIn = loginService.create(studentLogin);
		}
		else
		{
			loggedIn.setLastLoginTime(LocalDateTime.now());
			loggedIn.setLoginFlag(AppConstants.YES);
			loginService.update(loggedIn);
			
		}
		return loggedIn;
	}
	
	public void logout(final String emailId, final String deviceId)
	{
		StudentLogin loggedIn = loginService.getStudenLogin(emailId, deviceId);
		if(loggedIn!=null)
		{
			loggedIn.setLoginFlag(AppConstants.NO);
			loginService.update(loggedIn);
		}
	}
	
	public StudentLogin getLoginDetails(final String deviceId, final String emailId)
	{
		StudentLogin loggedIn = loginService.getStudenLogin(emailId, deviceId);
		return loggedIn;
		
	}

}
