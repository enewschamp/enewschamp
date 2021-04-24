package com.enewschamp.app.common;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HeaderDTO implements Serializable {
	private static final long serialVersionUID = -2727382165795060076L;
	private RequestStatusType requestStatus;
	@NotNull(message = "Module must not be null")
	@NotEmpty(message = "Module must not be empty")
	private String module;
	@NotNull(message = "LoginCredential must not be null")
	private String loginCredentials;
	@NotNull(message = "PageName must not be null")
	@NotEmpty(message = "PageName must not be empty")
	private String pageName;
	@NotNull(message = "Action must not be null")
	@NotEmpty(message = "Action must not be empty")
	private String action;
	@NotNull(message = "Action must not be null")
	private String operation;
	private String editionId;
	private LocalDate publicationDate;
	private String studentKey;
	private String emailId;
	private String appVersion;
	@NotNull(message = "DeviceId must not be null")
	@NotEmpty(message = "DeviceId must not be empty")
	private String deviceId;
	@NotNull(message = "UserId must not be null")
	@NotEmpty(message = "UserId must not be empty")
	private String userId;
	private Long studentId;
	private String helpText;
	private LocalDate todaysDate;
	private String propertiesLabel;
	private String lastPropChangeTime;

	@JsonIgnore
	public String getLoginCredentials() {
		return this.loginCredentials;
	}

	@JsonIgnore
	public String getUserId() {
		return this.userId;
	}

	@JsonIgnore
	public String getDeviceId() {
		return this.deviceId;
	}

	@JsonProperty
	public void setLoginCredentials(String loginCredentials) {
		this.loginCredentials = loginCredentials;
	}

	@JsonProperty
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@JsonProperty
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
