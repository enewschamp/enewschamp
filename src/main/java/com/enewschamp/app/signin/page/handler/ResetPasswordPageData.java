package com.enewschamp.app.signin.page.handler;

import com.enewschamp.app.common.PageData;

import lombok.Data;

@Data
public class ResetPasswordPageData extends PageData implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String emailId;
	private String securityCode;
	private String currentPassword;
	private String newPassword;
	private String confirmNewPassword;
	private String message;
}
