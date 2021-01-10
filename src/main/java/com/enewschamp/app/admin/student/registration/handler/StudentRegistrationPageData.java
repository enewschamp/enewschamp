package com.enewschamp.app.admin.student.registration.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

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
	private String password1;
	private String password2;
	@NotNull(message = MessageConstants.IS_DELETED_NOT_NULL)
	@NotEmpty(message = MessageConstants.IS_DELETED_NOT_EMPTY)
	private String isDeleted;
	@NotNull(message = MessageConstants.EMAIL_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EMAIL_ID_NOT_EMPTY)
	private String isAccountLocked;
	private LocalDateTime lastSuccessfulLoginAttempt;
	private LocalDateTime lastUnsuccessfulLoginAttempt;
	private long incorrectLoginAttempts;
	@NotNull(message = MessageConstants.IS_ACTIVE_NOT_NULL)
	@NotEmpty(message = MessageConstants.IS_ACTIVE_NOT_EMPTY)
	private String isActive;
	private String theme;
	private String fontHeight;
	private String photoName;
	private String avatarName;
	private String isTestUser;
	private String forcePasswordChange;
	private LocalDateTime creationDateTime;
}
