package com.enewschamp.app.common.country.entity;

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
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "Country", uniqueConstraints = { @UniqueConstraint(columnNames = { "nameId", "description" }) })
public class Country extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_id_generator")
	@SequenceGenerator(name = "country_id_generator", sequenceName = "country_id_seq", allocationSize = 1)
	@Column(name = "countryId", updatable = false, nullable = false)
	private Long countryId;

	@NotNull
	@Column(name = "nameId", length = 50)
	private String nameId;

	@NotNull
	@Column(name = "description", length = 50)
	private String description;

	@NotNull
	@Column(name = "isd", length = 4)
	private int isd;

	@NotNull
	@Column(name = "currencyId", length = 3)
	private String currencyId;

	@NotNull
	@Column(name = "languageId", length = 3)
	private String languageId;

}
