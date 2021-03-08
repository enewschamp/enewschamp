package com.enewschamp.app.student.scores.entity;

import java.time.LocalDate;

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
@Table(name = "ScoresDaily", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "studentId", "editionId", "publicationDate" }) })
@EqualsAndHashCode(callSuper = false)
public class ScoresDaily extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scores_daily_id_generator")
	@SequenceGenerator(name = "scores_daily_id_generator", sequenceName = "scores_daily_id_seq", allocationSize = 1)
	@Column(name = "scoresDailyId", updatable = false, nullable = false)
	private Long scoresDailyId;

	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;

	@NotNull
	@Column(name = "publicationDate")
	private LocalDate publicationDate;

	@Column(name = "articlesRead", length = 2)
	private Long articlesRead;

	@Column(name = "quizAttempted", length = 2)
	private Long quizAttempted;

	@Column(name = "quizCorrect", length = 2)
	private Long quizCorrect;

	@Column(name = "readingLevel", length = 1)
	private int readingLevel;

}
