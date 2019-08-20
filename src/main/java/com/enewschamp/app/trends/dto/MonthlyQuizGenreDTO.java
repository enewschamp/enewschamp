package com.enewschamp.app.trends.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyQuizGenreDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private LocalDate publishDate;
	
	
	private Long quizpublished;

	private Long quizAttempted;
	
	private Long quizcorrect;
	
	private String genreid;
	
}
