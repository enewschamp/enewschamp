package com.enewschamp.app.fw.page.navigation.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageNavigatorDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	private Long navId = 0L;
	private String action;
	private String currentPage;
	private String nextPage;
	private String nextPageOperation;
	private String nextPageLoadMethod;
	private String operation;
	private String updationTable;
	private String submissionMethod;
	private String workToMaster;
	private String controlWorkEntryOrExit;
	private String secured;
}
