package com.enewschamp.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "AppSecurity", uniqueConstraints={@UniqueConstraint(columnNames= {"module"})})
public class AppSecurity {

	private static final long serialVersionUID = -6268188630471167106L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_security_id_generator")
	@SequenceGenerator(name = "app_security_id_generator", sequenceName = "app_security_id_seq", allocationSize = 1)
	@Column(name = "appSecurityId", updatable = false, nullable = false)
	private Long appSecurityId;

	@NotNull
	@Column(name = "appName", length = 130)
	private String appName;

	@NotNull
	@Column(name = "appKey", length = 256)
	private String appKey;

	@NotNull
	@Column(name = "module", length = 20)
	private String module;

	@Column(name = "isAppAvailable", length = 1)
	private String isAppAvailable;

}
