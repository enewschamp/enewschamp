package com.enewschamp.app.common.city.entity;

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
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "City")
public class City extends BaseEntity {

	private static final long serialVersionUID = -6268188630471167106L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_id_generator")
	@SequenceGenerator(name = "city_id_generator", sequenceName = "cityid_seq", allocationSize = 1)
	@Column(name = "cityId", updatable = false, nullable = false)
	private Long cityId;

	@NotNull
	@Column(name = "nameId", length = 10)
	private String nameId;

	@NotNull
	@Column(name = "description", length = 50)
	private String description;

	@NotNull
	@Column(name = "stateId", length = 3)
	private String stateId;

	@NotNull
	@Column(name = "countryId", length = 2)
	private String countryId;

	@Column(name = "isApplicableForNewsEvents", length = 1)
	private String isApplicableForNewsEvents;

}
