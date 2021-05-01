package com.enewschamp.app.admin.user.role.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserRolePageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long userRoleId;

	@NotNull(message = MessageConstants.USER_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.USER_ID_NOT_EMPTY)
	private String userId;

	@NotNull(message = MessageConstants.ROLE_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.ROLE_ID_NOT_EMPTY)
	private String roleId;

	@NotNull(message = MessageConstants.COMMENTS_NOT_NULL)
	@NotEmpty(message = MessageConstants.COMMENTS_NOT_EMPTY)
	private String comments;
}
