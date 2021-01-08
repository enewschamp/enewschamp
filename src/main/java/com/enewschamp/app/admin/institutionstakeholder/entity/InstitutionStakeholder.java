package com.enewschamp.app.admin.institutionstakeholder.entity;

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
@Table(name = "InstitutionStakeholder")
public class InstitutionStakeholder extends BaseEntity {

	private static final long serialVersionUID = -6268188630471167106L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inst_stake_holder_id_generator")
	@SequenceGenerator(name = "inst_stake_holder_id_generator", sequenceName = "inst_stake_holder_id_seq", allocationSize = 1)
	@Column(name = "instStakeHolderId", updatable = false, nullable = false)
	private Long instStakeHolderId;

	@NotNull
	@Column(name = "stakeholderId")
	private Long stakeholderId;

	@NotNull
	@Column(name = "institutionId")
	private String institutionId;

	@NotNull
	@Column(name = "institutionType")
	private String institutionType;

	@NotNull
	private String comments;
}