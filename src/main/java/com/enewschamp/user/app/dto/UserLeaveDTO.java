package com.enewschamp.user.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.user.domain.entity.LeaveApprovalStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserLeaveDTO extends MaintenanceDTO {

	private static final long serialVersionUID = 2900828192606817270L;

	private UserLeaveKeyDTO userLeaveKey;

	@NotNull
	private LocalDate endDate;

	private int numberOfDays;

	private LeaveApprovalStatus approvalStatus;

	@NotNull
	private LocalDateTime applicationDateTime;

}
