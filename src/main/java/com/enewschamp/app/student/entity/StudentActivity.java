package com.enewschamp.app.student.entity;

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
@Entity
@Table(name = "StudentActivity")
@EqualsAndHashCode(callSuper = false)
public class StudentActivity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_activity_id_generator")
	@SequenceGenerator(name = "student_activity_id_generator", sequenceName = "student_activity_id_seq", allocationSize = 1)
	@Column(name = "studentActivityId", updatable = false, nullable = false)
	private Long studentActivityId;

	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId;

	@NotNull
	@Column(name = "newsArticleId", length = 10)
	private Long newsArticleId;

	@Column(name = "saved", length = 1)
	private String saved;

	@Column(name = "reaction", length = 1)
	private String reaction;

	@Column(name = "opinion", length = 400)
	private String opinion;

	@Column(name = "quizScore")
	private Long quizScore;

	@Column(name = "editionId", length = 10)
	private String editionId;

	@Column(name = "readingLevel", length = 1)
	private int readingLevel;
}