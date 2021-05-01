package com.enewschamp.app.common.uicontrols.dto;

import lombok.Data;

@Data
public class UIControlsGlobalDTO {

	private Long uiControlGlobalId;
	private String controlName;
	private String controlType;
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
	private String action;
	private String visibility;
	private String mandatory;
	private String regex;
	private String successMessage;
	private String errorMessage;
	private String confirmationMessage;
	private String isPremiumFeature;
	private String unavailableMessage;
}
