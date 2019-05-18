package com.enewschamp.user.app.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserRoleKeyDTO implements Serializable {

	private static final long serialVersionUID = 833455749882062454L;

	@NotNull
	@Size(max = ForeignKeyColumnLength.UserId)
	private String userId;

	@NotNull
	@Size(max = ForeignKeyColumnLength.RoleId)
	private String roleId;	

	@NotNull
	private WeekDayType dayOfTheWeek;

}
