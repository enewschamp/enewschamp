package com.enewschamp.app.fw.page.navigation.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PageNavigatorDTO extends AbstractDTO{

	private static final long serialVersionUID = 1L;

	private Long navId = 0L;
	private String action;
	private String currentPage;
	private String nextPage;
	private String nextPageOperation;
	private String previousPage;
	private String previousPageOperation;
	private String operation;
	private String commitMasterData;
	private String updationTable;
	private int processSeq;
	private String secured;

}
