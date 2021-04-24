package com.enewschamp.app.admin.user.leave.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.enewschamp.user.domain.entity.LeaveApprovalStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserLeavePageData extends PageData {
	private static final long serialVersionUID = 1L;

	@NotNull(message = MessageConstants.USER_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.USER_ID_NOT_EMPTY)
	private String userId;

	@NotNull(message = MessageConstants.START_DATE_NOT_NULL)
	private LocalDate startDate;

	@NotNull(message = MessageConstants.END_DATE_NOT_NULL)
	private LocalDate endDate;

	@NotNull(message = MessageConstants.NO_OF_DAYS_NOT_NULL)
	private int numberOfDays;

	private LeaveApprovalStatus approvalStatus;
	private LocalDateTime applicationDateTime;
	private String updateApplicationDateTime;
	private String comments;
	private String managerComments;

}
