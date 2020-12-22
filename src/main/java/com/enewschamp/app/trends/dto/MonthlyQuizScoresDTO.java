package com.enewschamp.app.trends.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyQuizScoresDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String month;

	private Long quizpublished;

	private Long quizAttempted;

	private Long quizcorrect;
}
