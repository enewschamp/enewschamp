package com.enewschamp.user.app.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserRoleKeyDTO implements Serializable {

	private static final long serialVersionUID = 833455749882062454L;

	@NotNull(message = MessageConstants.USER_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.USER_ID_NOT_EMPTY)
	@Size(max = ForeignKeyColumnLength.UserId)
	private String userId;

	@NotNull(message = MessageConstants.ROLE_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.USER_ID_NOT_EMPTY)
	@Size(max = ForeignKeyColumnLength.RoleId)
	private String roleId;
}
