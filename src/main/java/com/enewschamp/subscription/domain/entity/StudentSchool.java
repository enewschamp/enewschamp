package com.enewschamp.subscription.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentSchool")
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

	@NotNull
	private String state;

	private String stateNotInTheList;

	@NotNull
	private String city;

	private String cityNotInTheList;

	private String school;

	private String schoolNotInTheList;

	@NotNull
	@Column(name = "section", length = 20)
	private String section;

	@NotNull
	@Column(name = "grade", length = 10)
	private String grade;

	@Column(name = "approvalRequired", length = 1)
	private String approvalRequired;
}
