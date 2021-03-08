package com.enewschamp.app.student.scores.dto;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.enewschamp.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class ScoresDailyDTO extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@JsonInclude
	private Long scoresDailyId;

	@JsonInclude
	private Long studentId;

	@JsonInclude
	private String editionId;

	@JsonInclude
	private LocalDate publicationDate;

	@JsonInclude
	private Long articlesRead;

	@JsonInclude
	private Long quizAttempted;

	@JsonInclude
	private Long quizCorrect;

	@JsonInclude
	private int readingLevel;
}
