package com.enewschamp.app.admin.stakeholder.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class StakeHolderPageData extends PageData {
	private static final long serialVersionUID = 1L;
	
	private Long stakeHolderId;
	private String title;

	@NotNull(message = MessageConstants.STAKE_HOLDER_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.STAKE_HOLDER_NAME_NOT_EMPTY)
	private String name;

	@NotNull(message = MessageConstants.STAKE_HOLDER_SURNAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.STAKE_HOLDER_SURNAME_NOT_EMPTY)
	private String surname;

	private String otherNames;
	private String designation;
	private String pinCode;
	private String isATeacher;
	private String landLine1;
	private String landLine2;
	private String mobile1;
	private String mobile2;
	private String email1;
	private String email2;
	private String comments;

}
