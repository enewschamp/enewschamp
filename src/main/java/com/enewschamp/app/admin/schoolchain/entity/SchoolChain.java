package com.enewschamp.app.admin.schoolchain.entity;

import java.time.LocalDateTime;

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
@Table(name = "SchoolChain", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "countryId" }) })
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
	@Column(name = "countryId")
	private String countryId;

	@Column(name = "stateId")
	private String stateId;

	@Column(name = "cityId")
	private String cityId;
	@NotNull
	@Column(name = "presence")
	private String presence;
	
	@Column(name = "eduBoard")
	private String eduBoard;
	
	@Column(name = "eduMedium")
	private String eduMedium;

	@Column(name = "genderDiversity")
	private String genderDiversity;

	@Column(name = "feeStructure")
	private String feeStructure;

	@Column(name = "ownership")
	private String ownership;

	@Column(name = "schoolProgram")
	private String schoolProgram;

	@Column(name = "shiftDetails")
	private String shiftDetails;

	@Column(name = "studentResidences")
	private String studentResidences;

	@Column(name = "website")
	private String website;
	
	@NotNull
	@Column(name = "comments")
	private String comments;

	@Column(name = "operator")
	private String operator;
	
	@Column(name = "lastUpdate")
	private LocalDateTime lastUpdate;
}
