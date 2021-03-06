package com.enewschamp.app.admin.helpdesk.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HelpDeskPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long helpdeskId;
	private Long requestId;
	@NotNull(message = MessageConstants.STUDENT_ID_NOT_NULL)
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
	private String phoneNumber;
	private String supportingComments;
	private String closeFlag;
	private LocalDateTime createDateTime;
	private String supportUserId;
	private String supportComments;
	private LocalDateTime callbackDateTime;
}
