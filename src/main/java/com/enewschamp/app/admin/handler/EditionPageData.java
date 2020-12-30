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
public class EditionPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EDITION_ID_NOT_EMPTY)
	private String editionId; 
	@JsonInclude
	@NotNull(message = MessageConstants.EDITION_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.EDITION_NAME_NOT_EMPTY)
	private String editionName;
	@NotNull(message = MessageConstants.LANGUAGE_NOT_NULL)
	@JsonInclude
	private Long languageId;
}
