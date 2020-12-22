package com.enewschamp.app.trends.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyArticlesGenreDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private String genreId;

	@JsonInclude
	private Long articlesRead;

	@JsonInclude
	private Long articlesPublished;

}
