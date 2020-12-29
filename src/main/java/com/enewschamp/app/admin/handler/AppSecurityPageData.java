package com.enewschamp.app.admin.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AppSecurityPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long appSecId;
	@JsonInclude
	@NotNull(message = MessageConstants.APP_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.APP_NAME_NOT_EMPTY)
	private String appName;
	@JsonInclude
	@NotNull(message = MessageConstants.APP_KEY_NOT_NULL)
	@NotEmpty(message = MessageConstants.APP_KEY_NOT_EMPTY)
	private String appKey;
	@JsonInclude
	@NotNull(message = MessageConstants.MODULE_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.MODULE_NAME_NOT_EMPTY)
	private String module;
}
