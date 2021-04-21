package com.enewschamp.user.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.Id;

import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Embeddable
@MappedSuperclass
public class UserRoleKey implements Serializable {

	private static final long serialVersionUID = 3813939578047104408L;

	@Id
	@NotNull
	@Column(name = "UserId", length = ForeignKeyColumnLength.UserId)
	private String userId;

	@Id
	@NotNull
	@Column(name = "RoleId", length = ForeignKeyColumnLength.RoleId)
	private String roleId;

	@Id
	@NotNull
	@Column(name = "DayOfTheWeek")
	@Enumerated(EnumType.STRING)
	private WeekDayType dayOfTheWeek;

}