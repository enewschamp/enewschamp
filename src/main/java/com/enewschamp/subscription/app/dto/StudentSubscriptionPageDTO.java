package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.Page;
import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentSubscriptionPageDTO extends Page {

	
	private static final long serialVersionUID = 1L;
	
	StudentSubscriptionPageData data;
	UIControlsDTO screenProperties;
}
