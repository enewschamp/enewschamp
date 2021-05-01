package com.enewschamp.app.common.uicontrols.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UIControlsRulesDTO extends AbstractDTO {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private Long uiControlId = 0L;
	private Long ruleId = 0L;
	private Long execSeq;
	private String rule;
	private String visibility;
	private String action;
	private String mandatory;
}
