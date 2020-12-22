package com.enewschamp.app.trends.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyArticleDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private String month;

	@JsonInclude
	private Long articlesRead;

	@JsonInclude
	private Long articlesPublished;

}
