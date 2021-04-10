package com.enewschamp.app.admin.schoolsubscription.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SchoolSubscriptionGradePageData extends PageData {
	private static final long serialVersionUID = 1L;
	
	private Long schoolSubsGradeId;

	@NotNull(message = MessageConstants.SCHOOL_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.SCHOOL_ID_NOT_EMPTY)
	private String schoolId;

	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EDITION_ID_NOT_EMPTY)
	private String editionId;

	@NotNull(message = MessageConstants.GRADE_NOT_NULL)
	@NotEmpty(message = MessageConstants.GRADE_NOT_EMPTY)
	private String grade;
	
	@NotNull(message = MessageConstants.SECTION_NOT_NULL)
	@NotEmpty(message = MessageConstants.SECTION_NOT_EMPTY)
	private String section;
	
	private String comments;

}