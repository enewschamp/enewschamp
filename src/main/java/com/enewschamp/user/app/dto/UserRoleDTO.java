package com.enewschamp.user.app.dto;

import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserRoleDTO extends MaintenanceDTO {

	private static final long serialVersionUID = 3739220002839093095L;

	@Id
	private Long userRoleId;

	@NotNull
	private String module;

	@NotNull
	private String userId;

	@NotNull
	private String roleId;

	@NotNull
	private String comments;

}