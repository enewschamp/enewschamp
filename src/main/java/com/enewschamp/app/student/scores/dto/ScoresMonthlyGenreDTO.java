package com.enewschamp.app.student.scores.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.enewschamp.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class ScoresMonthlyGenreDTO extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@JsonInclude
	private Long scoresMonthlyGenreId;

	@JsonInclude
	private Long studentId;

	@JsonInclude
	private String editionId;

	@JsonInclude
	private Long yearMonth;

	@JsonInclude
	private String genreId;

	@JsonInclude
	private Long articlesRead;

	@JsonInclude
	private Long quizAttempted;

	@JsonInclude
	private Long quizCorrect;

	@JsonInclude
	private int readingLevel;

}
