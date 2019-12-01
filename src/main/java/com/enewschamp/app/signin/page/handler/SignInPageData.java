package com.enewschamp.app.signin.page.handler;

import com.enewschamp.app.common.PageData;

import lombok.Data;

@Data
public class SignInPageData extends PageData implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String emailId;
	private Long securityCode;
	private String password;
	private String verifyPassword;
	private String message;
	private String contactUsText;
}
