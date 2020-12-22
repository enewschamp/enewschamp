package com.enewschamp.app.scores.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class MonthlyScoreGenreDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonInclude
	private String genreId;

	@JsonInclude
	private Long articlesPublished;

	@JsonInclude
	private Long articlesRead;

	@JsonInclude
	private Long quizPublished;

	@JsonInclude
	private Long quizAttempted;

	@JsonInclude
	private Long quizCorrect;

}
