package com.enewschamp.app.student.scores.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "ScoresMonthlyGenre", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "studentId", "editionId", "scoreYearMonth", "genreId" }) })
@EqualsAndHashCode(callSuper = false)
public class ScoresMonthlyGenre extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scores_monthly_genre_id_generator")
	@SequenceGenerator(name = "scores_monthly_genre_id_generator", sequenceName = "scores_monthly_genre_id_seq", allocationSize = 1)
	@Column(name = "scoresMonthlyGenreId", updatable = false, nullable = false)
	private Long scoresMonthlyGenreId;

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

	@Column(name = "readingLevel", length = 1)
	private int readingLevel;

	@Column(name = "articlesRead", length = 3)
	private Long articlesRead = 0L;

	@Column(name = "quizAttempted", length = 2)
	private Long quizAttempted;

	@Column(name = "quizCorrect", length = 2)
	private Long quizCorrect;

	@PrePersist
	protected void beforePersist() {
		if (articlesRead == null)
			articlesRead = 0L;
	}

}
