package com.enewschamp.app.admin.stakeholder.entity;

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
@Table(name = "stakeholder",uniqueConstraints = {
		@UniqueConstraint(columnNames = { "name", "surname" }) })
public class StakeHolder extends BaseEntity {

	private static final long serialVersionUID = -6268188630471167106L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stakeholder_id_generator")
	@SequenceGenerator(name = "stakeholder_id_generator", sequenceName = "stakeholder_id_seq", allocationSize = 1)
	@Column(name = "stakeholderId", updatable = false, nullable = false)
	private Long stakeholderId;

	@Column(name = "title", length = 3)
	private String title;

	@NotNull
	@Column(name = "name", length = 50)
	private String name;

	@NotNull
	@Column(name = "surname", length = 50)
	private String surname;

	@Column(name = "otherNames", length = 99)
	private String otherNames;

	@Column(name = "designation", length = 50)
	private String designation;

	@Column(name = "isATeacher", length = 1)
	private String isATeacher;

	@Column(name = "landLine1", length = 20)
	private String landLine1;

	@Column(name = "landLine2", length = 20)
	private String landLine2;

	@Column(name = "mobile1", length = 20)
	private String mobile1;

	@Column(name = "mobile2", length = 20)
	private String mobile2;

	@Column(name = "email1", length = 99)
	private String email1;

	@Column(name = "email2", length = 99)
	private String email2;

	@Column(name = "comments", length = 999)
	private String comments;
}