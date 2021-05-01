package com.enewschamp.user.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "UserRoles", uniqueConstraints = { @UniqueConstraint(columnNames = { "userId", "module" }) })
public class UserRole extends BaseEntity {

	private static final long serialVersionUID = 2383120567523981474L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_id_generator")
	@SequenceGenerator(name = "user_role_id_generator", sequenceName = "user_role_id_seq", allocationSize = 1)
	@Column(name = "userRoleId", updatable = false, nullable = false)
	private Long userRoleId;

	@NotNull
	@Column(name = "module", length = 50)
	private String module;

	@Size(max = ForeignKeyColumnLength.UserId)
	private String userId;

	@NotNull
	@Size(max = ForeignKeyColumnLength.RoleId)
	private String roleId;

	@NotNull
	@Column(name = "comments", length = 300)
	private String comments;

}