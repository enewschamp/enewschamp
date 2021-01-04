package com.enewschamp.common.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "PropertiesBackend")
@EqualsAndHashCode(callSuper = false)
public class PropertiesBackend extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "properties_backend_id_generator")
	@SequenceGenerator(name = "properties_backend_id_generator", sequenceName = "properties_backend_id_seq", allocationSize = 1)
	@Column(name = "propertyId", length = 3)
	private Long propertyId = 0L;

	@NotNull
	@Column(name = "appName", length = 200)
	private String appName;

	@NotNull
	@Column(name = "name", length = 100)
	private String name;

	@NotNull
	@Column(name = "value", length = 4000)
	private String value;
}
