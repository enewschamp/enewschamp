package com.enewschamp.app.admin.institutionstakeholder.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InstitutionStakeholderPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long stakeHolderId;

	@NotNull(message = MessageConstants.INSTITUTION_ID_NOT_NULL)
	private Long institutionId;

	@NotNull(message = MessageConstants.INSTITUTION_TYPE_NOT_NULL)
	@NotEmpty(message = MessageConstants.INSTITUTION_TYPE_NOT_EMPTY)
	private String institutionType;

	private String comments;

}
