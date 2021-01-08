package com.enewschamp.app.student.badges.entity;

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
@Table(name = "StudentBadges", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "studentId", "badgeYearMonth", "badgeId" }) })
@EqualsAndHashCode(callSuper = false)
public class StudentBadges extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_badges_id_generator")
	@SequenceGenerator(name = "student_badges_id_generator", sequenceName = "student_badges_id_seq", allocationSize = 1)
	@Column(name = "studentBadgesId", updatable = false, nullable = false)
	private Long studentBadgesId;

	@NotNull
	@Column(name = "studentId", length = 10)
	private Long studentId;

	@Column(name = "badgeId", length = 3)
	private Long badgeId;

	@NotNull
	@Column(name = "badgeYearMonth")
	private Long yearMonth;
}
