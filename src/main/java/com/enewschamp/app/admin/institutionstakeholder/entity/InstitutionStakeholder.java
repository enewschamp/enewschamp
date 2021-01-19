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
@Table(name = "InstitutionStakeholders")
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
	@Column(name = "institutionId", length = 20)
	private Long institutionId;

	@NotNull
	@Column(name = "institutionType", length = 1)
	private String institutionType;

	@NotNull
	@Column(name = "comments", length = 999)
	private String comments;
}