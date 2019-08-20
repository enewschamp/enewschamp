package com.enewschamp.app.trends.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YearlyArticlesGenreDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer year;
	
	private Long articlesRead;
	
	private Long articlePublished;
	
	private String genreid;
	
	
}
