package com.enewschamp.user.app.dto;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserRoleDTO extends MaintenanceDTO {
	
	private static final long serialVersionUID = 3739220002839093095L;

	private UserRoleKeyDTO userRoleKey;

	@NotNull
	private int contribution = 0;


}
