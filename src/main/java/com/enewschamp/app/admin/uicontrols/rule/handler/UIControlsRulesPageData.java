package com.enewschamp.app.admin.uicontrols.rule.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class UIControlsRulesPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long ruleId;

    @NotNull(message = MessageConstants.UI_CONTROL_ID_NOT_NULL)
    @NotEmpty(message = MessageConstants.UI_CONTROL_ID_NOT_EMPTY)
	private Long uiControlId = 0L;

    @NotNull(message = MessageConstants.EXEC_SEQ_NOT_NULL)
    @NotEmpty(message = MessageConstants.EXEC_SEQ_NOT_EMPTY)
	private Long execSeq;

    @NotNull(message = MessageConstants.RULE_NOT_NULL)
    @NotEmpty(message = MessageConstants.RULE_NOT_EMPTY)
	private String rule;

    @NotNull(message = MessageConstants.VISIBILITY_NOT_NULL)
    @NotEmpty(message = MessageConstants.VISIBILITY_NOT_EMPTY)
	private String visibility;

}
