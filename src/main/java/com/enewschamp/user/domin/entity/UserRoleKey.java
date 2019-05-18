package com.enewschamp.user.domin.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
@MappedSuperclass
public class UserRoleKey implements Serializable {

	private static final long serialVersionUID = 3813939578047104408L;
	
	@NotNull
	@Column(name = "UserID", length = ForeignKeyColumnLength.UserId)
	private String userId;

	@NotNull
	@Column(name = "RoleID", length = ForeignKeyColumnLength.RoleId)
	private String roleId;	

	@NotNull
	@Column(name = "DayOfTheWeek")
	private WeekDayType dayOfTheWeek;

}
