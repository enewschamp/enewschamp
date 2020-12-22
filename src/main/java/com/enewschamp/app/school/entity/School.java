package com.enewschamp.app.school.entity;

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
@Table(name = "School")
@EqualsAndHashCode(callSuper = false)
public class School extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_id_generator")
	@SequenceGenerator(name = "school_id_generator", sequenceName = "school_seq", allocationSize = 1)
	@Column(name = "schoolId", updatable = false, nullable = false)
	private Long schoolId;

	@Column(name = "schoolChainId", length = 10)
	private Long schoolChainId;

	@Column(name = "schoolProgram", length = 1)
	private String schoolProgram;

	@Column(name = "schoolProgramCode", length = 50)
	private String schoolProgramCode;

	@NotNull
	@Column(name = "stateId", length = 50)
	private String stateId;

	@NotNull
	@Column(name = "countryId", length = 50)
	private String countryId;

	@NotNull
	@Column(name = "cityId", length = 50)
	private String cityId;

	@NotNull
	@Column(name = "name", length = 99)
	private String name;

	@Column(name = "website", length = 99)
	private String website;

	@Column(name = "ownership", length = 3)
	private String ownership;

	@Column(name = "eduBoard", length = 3)
	private String eduBoard;

	@Column(name = "eduMedium", length = 3)
	private String eduMedium;

	@NotNull
	@Column(name = "genderDiversity", length = 3)
	private String genderDiversity;

	@NotNull
	@Column(name = "studentResidences", length = 3)
	private String studentResidences;

	@Column(name = "shiftDetails", length = 999)
	private String shiftDetails;

	@Column(name = "feeStructure", length = 999)
	private String feeStructure;

	@Column(name = "comments", length = 999)
	private String comments;

}
