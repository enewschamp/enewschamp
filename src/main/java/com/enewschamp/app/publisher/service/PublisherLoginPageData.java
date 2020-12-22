package com.enewschamp.app.publisher.service;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;

import lombok.Data;

@Data
public class PublisherLoginPageData extends PageData {

	private static final long serialVersionUID = -6788157709824965592L;

	@NotNull
	private String userId;
	private String emailId;
	private String password;
	private String deviceId;
	private String message;
	private String passwordNew;
	private String passwordRepeat;
	private String securityCode;
	private String theme;
}
