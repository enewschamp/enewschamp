package com.enewschamp.app.admin.student.registration.handler;

import java.time.LocalDateTime;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentRegistrationPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private long studentId;
	private String emailId;
	private String password;
    private String confirmPassword;
	private String isDeleted;
	private String isAccountLocked;
	private LocalDateTime lastSuccessfulLoginAttempt;
	private LocalDateTime lastUnsuccessfulLoginAttempt;
	private long incorrectLoginAttempts;
	private String isActive;
	private String theme;
	private String fontHeight;
	private String avatarName;
	private String isTestUser;
	private String forcePasswordChange;
	private LocalDateTime creationDateTime;
	private String imageName;
	private String imageBase64;
	private String imageTypeExt;
	private String imageUpdate;
	private String studentKey;
	private String photoName;
	private String imageApprovalRequired;
	private String fcmToken;
	
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}
	
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}
	
	@JsonIgnore
	public LocalDateTime getCreationDateTime() {
		return this.creationDateTime;
	} 
	
	@JsonProperty
	public void setPassword(LocalDateTime creationDateTime) {
		this.creationDateTime =creationDateTime;
	}

}