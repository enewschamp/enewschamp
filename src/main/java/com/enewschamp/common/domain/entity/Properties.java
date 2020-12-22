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
@Table(name = "Properties")
@EqualsAndHashCode(callSuper = false)
public class Properties extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "property_id_generator")
	@SequenceGenerator(name = "property_id_generator", sequenceName = "property_id_seq", allocationSize = 1)
	@Column(name = "propertyId", length = 3)
	private Long propertyId = 0L;

	@NotNull
	@Column(name = "propertyCategory", length = 50)
	private String propertyCategory;

	@NotNull
	@Column(name = "propertyName", length = 50)
	private String propertyName;

	@NotNull
	@Column(name = "propertyValue", length = 200)
	private String propertyValue;

	@NotNull
	@Column(name = "backendOnly", length = 1)
	private String backendOnly;
}
