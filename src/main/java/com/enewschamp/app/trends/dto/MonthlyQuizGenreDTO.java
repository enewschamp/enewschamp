package com.enewschamp.app.trends.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyQuizGenreDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private String month;

	@JsonInclude
	private Long quizPublished;

	@JsonInclude
	private Long quizAttempted;

	@JsonInclude
	private Long quizCorrect;

}
