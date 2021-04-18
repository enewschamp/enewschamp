package com.enewschamp.app.admin.page.navigator.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageNavigatorPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long navId;

	@NotNull(message = MessageConstants.CURRENT_PAGE_NOT_NULL)
	@NotEmpty(message = MessageConstants.CURRENT_PAGE_NOT_EMPTY)
	private String currentPage;

	@NotNull(message = MessageConstants.OPERATION_NOT_NULL)
	@NotEmpty(message = MessageConstants.OPERATION_NOT_EMPTY)
	private String operation;

	@NotNull(message = MessageConstants.ACTION_NOT_NULL)
	@NotEmpty(message = MessageConstants.ACTION_NOT_EMPTY)
	private String action;

	@NotNull(message = MessageConstants.NEXT_PAGE_NOT_NULL)
	@NotEmpty(message = MessageConstants.NEXT_PAGE_NOT_EMPTY)
	private String nextPage;

	private String submissionMethod;
	private String workToMaster;

	@NotNull(message = MessageConstants.NEXT_PAGE_OPERATION_NOT_NULL)
	@NotEmpty(message = MessageConstants.NEXT_PAGE_OPERATION_NOT_EMPTY)
	private String nextPageOperation;

	private String nextPageLoadMethod;
	private String controlWorkEntryOrExit;
	private String updationTable;
}
