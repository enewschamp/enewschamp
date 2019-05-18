package com.enewschamp.user.domin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="Role")
public class Role extends BaseEntity {

	private static final long serialVersionUID = -613430239050572428L;

	@Id
	@NotNull
	@Column(name = "RoleID", length = 10)
	private String roleId;
	
	@NotNull
	@Column(name = "Name", length = 50)
	private String roleName;
	
}
