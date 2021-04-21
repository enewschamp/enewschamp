package com.enewschamp.app.admin.entitlement.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "Entitlements", uniqueConstraints = { @UniqueConstraint(columnNames = { "userId", "role", "pageName" }) })
@EqualsAndHashCode(callSuper = false)
public class Entitlement extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entitlement_id_generator")
	@SequenceGenerator(name = "entitlement_id_generator", sequenceName = "entitlement_id_seq", allocationSize = 1)
	@Column(name = "entitlementId", updatable = false, nullable = false)
	private Long entitlementId;
	@NotNull
	@Column(name = "pageName", length = 50)
	private String pageName;

	@NotNull
	@Column(name = "role", length = 20)
	private String role;

	@NotNull
	@Column(name = "userId", length = 20)
	private String userId;
}
