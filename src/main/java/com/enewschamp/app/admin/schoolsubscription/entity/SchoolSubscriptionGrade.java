package com.enewschamp.app.admin.schoolsubscription.entity;

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
@Table(name = "SchoolSubscriptionGrades")
public class SchoolSubscriptionGrade extends BaseEntity {

	private static final long serialVersionUID = -6268188630471167106L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_subscription_grade_id_generator")
	@SequenceGenerator(name = "school_subscription_grade_id_generator", sequenceName = "school_subscription_grade_id_seq", allocationSize = 1)
	@Column(name = "schoolSubsGradeId", updatable = false, nullable = false)
	private Long schoolSubsGradeId;

	@NotNull
	@Column(name = "schoolId", length = 20)
	private String schoolId;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;

	@NotNull
	@Column(name = "grade", length = 20)
	private String grade;

	@NotNull
	@Column(name = "section", length = 20)
	private String section;

	@Column(name = "capacity", length = 20)
	private Long capacity;
}