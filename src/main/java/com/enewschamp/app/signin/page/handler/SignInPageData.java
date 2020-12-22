package com.enewschamp.app.signin.page.handler;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class SignInPageData extends PageData implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private String emailId;
	@JsonInclude
	private String password;
	@JsonInclude
	private String message;
}
