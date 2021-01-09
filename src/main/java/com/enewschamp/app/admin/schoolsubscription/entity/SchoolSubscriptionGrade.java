package com.enewschamp.app.admin.schoolsubscription.entity;

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
@Table(name = "SchoolSubscriptionGrades", uniqueConstraints={@UniqueConstraint(columnNames={"stakeholderId", "schoolId", "editionId", "grade", "section"})})
public class SchoolSubscriptionGrade extends BaseEntity {

	private static final long serialVersionUID = -6268188630471167106L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_subscription_id_generator")
	@SequenceGenerator(name = "school_subscription_id_generator", sequenceName = "school_subscription_id_seq", allocationSize = 1)
	@Column(name = "schoolSubscriptionId", updatable = false, nullable = false)
	private Long schoolSubscriptionId;

	@NotNull
	@Column(name = "stakeholderId")
	private Long stakeholderId;

	@NotNull
	@Column(name = "schoolId")
	private String schoolId;

	@NotNull
	@Column(name = "editionId")
	private String editionId;

	@NotNull
	@Column(name = "grade")
	private String grade;
	
	@NotNull
	@Column(name = "section")
	private String section;
	
	@Column(name = "comments")
	private String comments;
}