package com.enewschamp.subscription.domain.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.StringCryptoConverter;
import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentSchool",uniqueConstraints = {
		@UniqueConstraint(columnNames = { "state", "country", "city", "school" }) })
public class StudentSchool extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId = 0L;

	@NotNull
	private String country;

	private String countryNotInTheList;

	@Convert(converter = StringCryptoConverter.class)
	@NotNull
	private String state;

	private String stateNotInTheList;

	@Convert(converter = StringCryptoConverter.class)
	@NotNull
	private String city;

	private String cityNotInTheList;

	@Convert(converter = StringCryptoConverter.class)
	private String school;

	private String schoolNotInTheList;

	@Convert(converter = StringCryptoConverter.class)
	@NotNull
	@Column(name = "section", length = 50)
	private String section;

	@Convert(converter = StringCryptoConverter.class)
	@NotNull
	@Column(name = "grade", length = 50)
	private String grade;

	@Column(name = "approvalRequired", length = 1)
	private String approvalRequired;
}