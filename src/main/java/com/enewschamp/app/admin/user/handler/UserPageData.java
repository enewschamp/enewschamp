package com.enewschamp.app.admin.user.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.enewschamp.domain.common.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserPageData extends PageData {
	private static final long serialVersionUID = 1L;

	@NotNull(message = MessageConstants.USER_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.USER_ID_NOT_EMPTY)
	private String userId;

	private String title;

	@NotNull(message = MessageConstants.USER_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.USER_NAME_NOT_EMPTY)
	private String name;

	@NotNull(message = MessageConstants.SURNAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.SURNAME_NOT_EMPTY)
	private String surname;

	private String otherNames;
	private Gender gender;
	private LocalDate doB;
	private LocalDate contractStartDate;
	private LocalDate contractEndDate;
	private String mobileNumber1;
	private String mobileNumber2;
	private String landline1;
	private String landline2;
	private String emailId1;
	private String emailId2;
	private String comments;
	private String password;
	private String password1;
	private String password2;

	private String isAccountLocked;

	private LocalDateTime lastSuccessfulLoginAttempt;
	private LocalDateTime lastUnsuccessfulLoginAttempt;
	private long incorrectLoginAttempts;

	private String isActive;

	private String theme;
	private String fontHeight;
	private LocalDateTime creationDateTime;
	private String imageBase64;
	private String imageTypeExt = "jpg";
	@JsonInclude
	private String imageName;
	private String imageUpdate;

}
