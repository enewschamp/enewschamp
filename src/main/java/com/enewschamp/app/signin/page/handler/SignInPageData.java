package com.enewschamp.app.signin.page.handler;

import lombok.Data;

@Data
public class SignInPageData implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String emailId;
	private Long securityCode;
	private String password;
	private String verifypassword;
}
