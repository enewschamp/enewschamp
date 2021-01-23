package com.enewschamp.user.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "UserLeaves")
public class UserLeave extends BaseEntity {

	private static final long serialVersionUID = -1254968870368475725L;

	@EmbeddedId
	private UserLeaveKey userLeaveKey;

	@NotNull
	@Column(name = "EndDate")
	private LocalDate endDate;

	@Column(name = "NumberOfDays")
	private int numberOfDays;

	@NotNull
	@Column(name = "ApprovalStatus")
	@Enumerated(EnumType.STRING)
	private LeaveApprovalStatus approvalStatus;

	@NotNull
	@Column(name = "ApplicationDateTime")
	private LocalDateTime applicationDateTime;
	
	@Column(name = "Comments", length = 200)
	private String comments;
	
	@Column(name = "ManagerComments", length = 200)
	private String managerComments;

}
