package com.enewschamp.app.student.registration.dto;

import java.time.LocalDateTime;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentRegistrationDTO extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String emailId;
	private String password;
	private String isDeleted;
	private LocalDateTime lastLoginTime;
	private String theme;
	private String fontHeight;
	private String photoName;
	private String avatarName;
}
