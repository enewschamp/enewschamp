package com.enewschamp.app.admin.errorcode.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ErrorCodesPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long errorCodeId;

	@NotNull(message = MessageConstants.ERROR_CATEGORY_NOT_NULL)
	@NotEmpty(message = MessageConstants.ERROR_CATEGORY_NOT_EMPTY)
	private String errorCategory;

	private String errorDescription;

	@NotNull(message = MessageConstants.ERROR_CODE_NOT_NULL)
	@NotEmpty(message = MessageConstants.ERROR_CODE_NOT_EMPTY)
	private String errorCode;

	@NotNull(message = MessageConstants.ERROR_MESSAGE_NOT_NULL)
	@NotEmpty(message = MessageConstants.ERROR_MESSAGE_NOT_EMPTY)
	private String errorMessage;
}
