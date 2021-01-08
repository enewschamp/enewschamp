package com.enewschamp.app.admin.stakeholder.entity;

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
@Table(name = "StakeHolder")
public class StakeHolder extends BaseEntity {

	private static final long serialVersionUID = -6268188630471167106L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stake_holder_id_generator")
	@SequenceGenerator(name = "stake_holder_id_generator", sequenceName = "stake_holder_id_seq", allocationSize = 1)
	@Column(name = "stakeHolderId", updatable = false, nullable = false)
	private Long stakeHolderId;

	@Column(name = "title")
	private String title;

	@NotNull
	@Column(name = "institutionId")
	private String institutionId;

	@NotNull
	@Column(name = "institutionType")
	private String institutionType;

	@NotNull
	@Column(name = "name")
	private String name;

	@NotNull
	@Column(name = "surname")
	private String surname;

	@Column(name = "otherNames")
	private String otherNames;

	@Column(name = "designation")
	private String designation;

	@Column(name = "pinCode")
	private String pinCode;

	@Column(name = "isATeacher")
	private String isATeacher;
	@Column(name = "landLine1")
	private String landLine1;
	@Column(name = "landLine2")
	private String landLine2;
	@Column(name = "mobile1")
	private String mobile1;
	@Column(name = "mobile2")
	private String mobile2;
	@Column(name = "email1")
	private String email1;
	@Column(name = "email2")
	private String email2;
	@Column(name = "comments")
	private String comments;
}