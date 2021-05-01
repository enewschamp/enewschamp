package com.enewschamp.app.admin.student.registration.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
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
	@NotNull(message = MessageConstants.EMAIL_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EMAIL_ID_NOT_EMPTY)
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
	private String imageTypeExt = "jpg";
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

}