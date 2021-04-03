package com.enewschamp.app.signin.page.handler;

import java.io.Serializable;
import java.time.LocalDate;

import com.enewschamp.app.common.PageData;

import lombok.Data;

@Data
public class LoginPageData extends PageData implements Serializable {

	private static final long serialVersionUID = -7777503117172470732L;
	private String emailId;
	private String password;
	private String deviceId;
	private String message;
	private String userName;
	private LocalDate todaysDate;
	private String theme;
	private String userRole;
	private String loginCredentials;
	private String tokenValidity;
	private String imageName;
}
