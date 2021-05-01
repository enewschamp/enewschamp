package com.enewschamp.app.student.scores.entity;

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
@Table(name = "ScoresMonthlyTotal", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "studentId", "editionId", "scoreYearMonth" }) })
@EqualsAndHashCode(callSuper = false)
public class ScoresMonthlyTotal extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scores_monthly_total_id_generator")
	@SequenceGenerator(name = "scores_monthly_total_id_generator", sequenceName = "scores_monthly_total_id_seq", allocationSize = 1)
	@Column(name = "scoresMonthlyTotalId", updatable = false, nullable = false)
	private Long scoresMonthlyTotalId;

	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;

	@Column(name = "scoreYearMonth")
	private Long yearMonth;

	@Column(name = "genreId", length = 12)
	private String genreId;

	@Column(name = "articlesRead", length = 3)
	private Long articlesRead;

	@Column(name = "quizAttempted", length = 2)
	private Long quizAttempted;

	@Column(name = "quizCorrect", length = 2)
	private Long quizCorrect;

	@Column(name = "readingLevel", length = 1)
	private int readingLevel;

}