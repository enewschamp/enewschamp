package com.enewschamp.app.admin.schoolchain.entity;

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
@Table(name = "SchoolChain",uniqueConstraints = {
		@UniqueConstraint(columnNames = { "name", "countryId" }) })
@EqualsAndHashCode(callSuper = false)
public class SchoolChain extends BaseEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_chain_id_generator")
	@SequenceGenerator(name = "school_chain_id_generator", sequenceName = "school_chain_id_seq", allocationSize = 1)
	@Column(name = "schoolChainId", updatable = false, nullable = false)
	private Long schoolChainId;

	@NotNull
	@Column(name = "name", length = 99)
	private String name;

	@NotNull
	@Column(name = "countryId", length = 50)
	private String countryId;

	@Column(name = "stateId", length = 50)
	private String stateId;

	@Column(name = "cityId", length = 50)
	private String cityId;

	@NotNull
	@Column(name = "presence", length = 1)
	private String presence;

	@Column(name = "eduBoard", length = 3)
	private String eduBoard;

	@Column(name = "eduMedium", length = 3)
	private String eduMedium;

	@Column(name = "genderDiversity", length = 3)
	private String genderDiversity;

	@Column(name = "feeStructure", length = 999)
	private String feeStructure;

	@Column(name = "ownership", length = 3)
	private String ownership;

	@Column(name = "schoolProgram", length = 1)
	private String schoolProgram;

	@Column(name = "shiftDetails", length = 999)
	private String shiftDetails;

	@Column(name = "studentResidences", length = 3)
	private String studentResidences;

	@Column(name = "website", length = 99)
	private String website;

	@NotNull
	@Column(name = "comments", length = 999)
	private String comments;

}
