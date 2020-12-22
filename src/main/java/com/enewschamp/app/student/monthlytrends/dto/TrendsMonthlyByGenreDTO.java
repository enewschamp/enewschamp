package com.enewschamp.app.student.monthlytrends.dto;

import com.enewschamp.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TrendsMonthlyByGenreDTO extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@JsonInclude
	private Long trendsMonthlyGenreId;

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
