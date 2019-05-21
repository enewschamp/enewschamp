package com.enewschamp.subscription.app.dto;

import java.util.List;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentPreferencePageDTO extends PageDTO {

private static final long serialVersionUID = 1L;
	
	StudentPreferencePageData data;
	List<UIControlsDTO> screenProperties;
}
