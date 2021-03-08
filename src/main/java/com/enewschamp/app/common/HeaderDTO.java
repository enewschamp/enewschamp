package com.enewschamp.app.common;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HeaderDTO implements Serializable {

	private static final long serialVersionUID = -2727382165795060076L;

	private RequestStatusType requestStatus;
	private String module;
	private String loginCredentials;
	private String pageName;
	private String action;
	private String operation;
	private String editionId;
	private LocalDate publicationDate;
	private String studentKey;
	private String emailId;
	private String appVersion;
	private String deviceId;
	private String userId;
	private String helpText;
	private LocalDate todaysDate;
	private String propertiesLabel;
}
