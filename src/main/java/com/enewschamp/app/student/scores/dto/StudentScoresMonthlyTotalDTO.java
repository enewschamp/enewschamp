package com.enewschamp.app.student.scores.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.enewschamp.app.common.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Entity
public class StudentScoresMonthlyTotalDTO extends AbstractDTO {

	@Id
	private String month;

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
