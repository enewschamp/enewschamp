package com.enewschamp.app.admin.user.login.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.user.login.entity.UserType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserLoginPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long userLoginId;

	@NotNull(message = MessageConstants.USER_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.USER_ID_NOT_EMPTY)
	private String userId;

	@NotNull(message = MessageConstants.LOGIN_FLAG_NOT_NULL)
	@NotEmpty(message = MessageConstants.LOGIN_FLAG_NOT_EMPTY)
	private String loginFlag;

	private LocalDateTime lastLoginTime;

	private LocalDateTime tokenExpirationTime;

	private String tokenId;

	@NotNull(message = MessageConstants.DEVICE_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.DEVICE_ID_NOT_EMPTY)
	private String deviceId;

	@NotNull(message = MessageConstants.USER_TYPE_NOT_NULL)
	private UserType userType;
	private String status;

}
