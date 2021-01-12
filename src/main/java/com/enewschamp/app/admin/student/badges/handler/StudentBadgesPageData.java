package com.enewschamp.app.admin.student.badges.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentBadgesPageData extends PageData {
	private static final long serialVersionUID = 1L;

	@NotNull(message = MessageConstants.STUDENT_BADGE_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.STUDENT_BADGE_ID_NOT_EMPTY)
	private Long studentBadgesId;

	@NotNull(message = MessageConstants.STUDENT_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.STUDENT_ID_NOT_EMPTY)
	private Long studentId;

	@NotNull(message = MessageConstants.BADGE_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.BADGE_ID_NOT_EMPTY)
	private Long badgeId;

	@NotNull(message = MessageConstants.BADGE_YEAR_MONTH_NOT_NULL)
	private Long yearMonth;
}
