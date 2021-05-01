package com.enewschamp.app.student.scores.dto;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.enewschamp.app.common.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class StudentScoresDailyDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@JsonInclude
	private LocalDate publicationDate;

	@JsonInclude
	private Long articlesPublished;

	@JsonInclude
	private Long articlesRead;

	@JsonInclude
	private Long quizPublished;

	@JsonInclude
	private Long quizAttempted;

	@JsonInclude
	private Long quizCorrect;
}
