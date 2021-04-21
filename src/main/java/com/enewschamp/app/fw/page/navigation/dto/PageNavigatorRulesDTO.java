package com.enewschamp.app.fw.page.navigation.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageNavigatorRulesDTO extends AbstractDTO {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private Long navId = 0L;

	private Long ruleId = 0L;

	private Long execSeq;

	private String rule;

	private String nextPage;

	private String nextPageOperation;

	private String nextPageLoadMethod;

	private String controlWorkEntryOrExit;

}
