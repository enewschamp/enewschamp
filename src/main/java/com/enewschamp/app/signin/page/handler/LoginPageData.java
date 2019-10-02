package com.enewschamp.app.signin.page.handler;

import com.enewschamp.app.common.PageData;

import lombok.Data;

@Data
public class LoginPageData extends PageData implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String emailId;
	private String password;
	private String deviceId;
	private String message;
	
}
