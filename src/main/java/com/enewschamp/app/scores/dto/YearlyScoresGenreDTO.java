package com.enewschamp.app.scores.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class YearlyScoresGenreDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private String genreid;
	private Long articlesPiblished;
	private Long articlesRead;
	private Long quizPublished;
	private Long quizAttempted;
	private Long quizCorerct;
}
