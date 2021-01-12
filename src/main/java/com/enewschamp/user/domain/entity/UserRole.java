package com.enewschamp.user.domain.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "UserRoles")
public class UserRole extends BaseEntity {

	private static final long serialVersionUID = 2383120567523981474L;

	@EmbeddedId
	private UserRoleKey userRoleKey;

	@NotNull
	@Column(name = "Contribution")
	private int contribution = 0;
	
	@Column(name = "Comments", length = 300)
	private String comments;

	@Column(name = "Contributions", length = 300)
	private String contributions;
}
