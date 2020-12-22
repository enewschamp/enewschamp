package com.enewschamp.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "AppSecurity")
public class AppSecurity {

	private static final long serialVersionUID = -6268188630471167106L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appsec_id_generator")
	@SequenceGenerator(name = "appsec_id_generator", sequenceName = "appsec_id_seq", allocationSize = 1)
	@Column(name = "appSecId", updatable = false, nullable = false)
	private Long appSecId;

	@NotNull
	@Column(name = "appName", length = 130)
	private String appName;

	@NotNull
	@Column(name = "appKey", length = 256)
	private String appKey;

	@NotNull
	@Column(name = "module", length = 20)
	private String module;

}
