package com.enewschamp.app.admin.entitlement.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EntitlementPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private Long entitlementId;
	@NotNull(message = MessageConstants.USER_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.USER_ID_NOT_EMPTY)
	private String userId;

	@NotNull(message = MessageConstants.USER_ROLE_NOT_NULL)
	@NotEmpty(message = MessageConstants.USER_ROLE_NOT_EMPTY)
	private String role;

	@NotNull(message = MessageConstants.PAGE_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.PAGE_NAME_NOT_EMPTY)
	private String pageName;
}
