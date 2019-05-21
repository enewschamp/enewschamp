package com.enewschamp.app.common.uicontrols.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UIControlsDTO {

	private Long uiControlId;
	private String screenName;
	private String controlName;
	private String dataSize;
	private String dataType;
	private Date creationTime;
	private String controlType;
	private String visibleOnScreen;
	private String enabledOnScreen;
}
