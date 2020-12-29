package com.enewschamp.app.admin.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HelpDeskPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private Long id;
	private Long requestId;
	@NotNull(message = MessageConstants.STUDENT_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.STUDENT_ID_NOT_EMPTY)
	private Long studentId;
	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EDITION_ID_NOT_EMPTY)
	private String editionId;
	@NotNull(message = MessageConstants.CATEGORY_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.CATEGORY_ID_NOT_EMPTY)
	private String categoryId;
	@NotNull(message = MessageConstants.DETAILS_NOT_NULL)
	@NotEmpty(message = MessageConstants.DETAILS_NOT_EMPTY)
	private String details;
	@NotNull(message = MessageConstants.CALLBACK_PHONE_NO_NOT_NULL)
	@NotEmpty(message = MessageConstants.CALLBACK_PHONE_NO_NOT_EMPTY)
	private String callBackPhoneNumber;
	private String supportingComments;
	private String closeFlag;
	@NotNull(message = MessageConstants.CALLBACK_TIME_NOT_NULL)
	@NotEmpty(message = MessageConstants.CALLBACK_TIME_NOT_EMPTY)
	private LocalDateTime callBackTime;
	private LocalDateTime createDateTime;
}
