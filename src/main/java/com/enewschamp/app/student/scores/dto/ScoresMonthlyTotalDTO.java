package com.enewschamp.app.student.scores.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ScoresMonthlyTotalDTO extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long scoresMonthlyTotalId;

	private Long studentId;

	private String editionId;

	private String yearMonth;

	private String genreId;

	private Long articlesRead;

	private Long quizAttempted;

	private Long quizCorrect;

	private int readingLevel;

}
