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
import javax.validation.constraints.NotEmpty;
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

	@NotNull
	@Column(name = "stateId")
	private String stateId;

	@NotNull
	@Column(name = "cityId")
	private String cityId;
	@NotNull
	@NotEmpty
	private String presence;
	@NotNull
	@NotEmpty
	private String eduBoard;
	@NotNull
	@NotEmpty
	private String eduMedium;
	@NotNull
	@NotEmpty
	private String genderDiversity;
	@NotNull
	@NotEmpty
	private String feeStructure;
	@NotNull
	@NotEmpty
	private String ownership;
	@NotNull
	@NotEmpty
	private String schoolProgram;
	@NotNull
	@NotEmpty
	private String shiftDetails;
	@NotNull
	@NotEmpty
	private String studentResidences;
	@NotNull
	@NotEmpty
	private String website;
	@NotNull
	@NotEmpty
	private String comments;
	@NotNull
	@NotEmpty
	private String operator;
	@NotNull
	@NotEmpty
	private LocalDateTime lastUpdate;
}
