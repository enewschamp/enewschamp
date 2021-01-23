package com.enewschamp.app.admin.page.navigator.rule.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageNavigatorRulesPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long ruleId;

	@NotNull(message = MessageConstants.NAV_ID_NOT_NULL)
	private Long navId;

	@NotNull(message = MessageConstants.EXEC_SEQUENCE_NOT_NULL)
	private Long execSeq;

	@NotNull(message = MessageConstants.RULE_NOT_NULL)
	@NotEmpty(message = MessageConstants.RULE_NOT_EMPTY)
	private String rule;

	private String nextPage;
	private String nextPageOperation;
	private String nextPageLoadMethod;

	@NotNull(message = MessageConstants.CONTROL_WORK_ENTRY_NOT_NULL)
	@NotEmpty(message = MessageConstants.CONTROL_WORK_ENTRY_NOT_EMPTY)
	private String controlWorkEntryOrExit;
}
