package com.enewschamp.app.trends.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyQuizScoresDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private LocalDate publicationDate;

	@JsonInclude
	private Long quizpublished;

	@JsonInclude
	private Long quizAttempted;

	@JsonInclude
	private LocalDate quizcorrect;
}
