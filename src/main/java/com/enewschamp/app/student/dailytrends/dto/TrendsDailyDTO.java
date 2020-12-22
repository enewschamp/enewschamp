package com.enewschamp.app.student.dailytrends.dto;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TrendsDailyDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private Long trendsDailyId;

	@JsonInclude
	private Long studentId;

	@JsonInclude
	private String editionId;

	@JsonInclude
	private LocalDate quizPublicationDate;

	@JsonInclude
	private Long articlesRead;

	@JsonInclude
	private Long quizAttempted;

	@JsonInclude
	private Long quizCorrect;

	@JsonInclude
	private int readingLevel;
}
