package com.enewschamp.app.scores.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DailyScoreDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer month;
	private Long articlesPiblished;
	private Long articlesRead;
	private Long quizPublished;
	private Long quizAttempted;
	private Long quizCorerct;
	
}
