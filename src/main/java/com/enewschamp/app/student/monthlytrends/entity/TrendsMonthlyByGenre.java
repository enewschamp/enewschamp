package com.enewschamp.app.student.monthlytrends.entity;

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
@Table(name = "TrendsMonthlyByGenre", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "studentId", "editionId", "trendyearMonth", "genreId" }) })
@EqualsAndHashCode(callSuper = false)
public class TrendsMonthlyByGenre extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trendsMonthlyGenre_id_generator")
	@SequenceGenerator(name = "trendsMonthlyGenre_id_generator", sequenceName = "trendsMonthlyGenre_seq", allocationSize = 1)
	@Column(name = "trendsMonthlyGenreId", updatable = false, nullable = false)
	private Long trendsMonthlyGenreId;

	@NotNull
	@Column(name = "studentId", length = 10)
	private Long studentId;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;

	@Column(name = "trendyearMonth")
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
