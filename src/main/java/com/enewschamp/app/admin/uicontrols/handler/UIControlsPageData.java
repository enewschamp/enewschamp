package com.enewschamp.app.admin.uicontrols.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UIControlsPageData extends PageData {
	private static final long serialVersionUID = 1L;
	
	private Long uiControlId;

	@NotNull(message = MessageConstants.PAGE_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.PAGE_NAME_NOT_EMPTY)
	private String pageName;

	@NotNull(message = MessageConstants.OPERATION_NOT_NULL)
	@NotEmpty(message = MessageConstants.OPERATION_NOT_EMPTY)
	private String operation;

	@NotNull(message = MessageConstants.CONTROL_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.CONTROL_NAME_NOT_EMPTY)
	private String controlName;

	private String controlType;

	@NotNull(message = MessageConstants.CONTROL_LABEL_NOT_NULL)
	@NotEmpty(message = MessageConstants.CONTROL_LABEL_NOT_EMPTY)
	private String controlLabel;

	private String dataType;
	private String globalControlRef;
	private String defaultValue;
	private String placeHolder;
	private String helpText;
	private String icon;
	private String iconType;
	private String keyboard;
	private String multiLine;
	private String minLength;
	private String maxLength;
	private String height;
	private String width;
	private String visibility;
	private String mandatory;
	private String regex;
	private String action;
	private String successMessage;
	private String errorMessage;
	private String confirmationMessage;
	private String isPremiumFeature;
	private String unavailableMessage;
}
