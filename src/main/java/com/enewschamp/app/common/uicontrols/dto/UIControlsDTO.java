package com.enewschamp.app.common.uicontrols.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UIControlsDTO {

	private Long uiControlId;
	private String screenName;
	private String controlName;
	private String controlLabel;
	private String dataSize;
	private String dataType;
	private Date creationTime;
	private String controlType;
	private String action;
	private String visibility;
	private String placeHolder;
	private Long minLength;
	private Long maxLength;
	private String regex;
	private String mandatory;
}
