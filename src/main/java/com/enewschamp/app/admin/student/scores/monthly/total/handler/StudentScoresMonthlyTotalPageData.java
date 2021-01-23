package com.enewschamp.app.admin.student.scores.monthly.total.handler;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class StudentScoresMonthlyTotalPageData extends PageData {
	private static final long serialVersionUID = 1L;
	
	private Long scoresMonthlyTotalId;
	
	private LocalDate publicationDate;

	@NotNull(message = MessageConstants.ARTICLE_READ_NOT_NULL)
	private Long articlesRead;

	@NotNull(message = MessageConstants.QUIZ_ATTEMPTED_NOT_NULL)
	private Long quizAttempted;

	@NotNull(message = MessageConstants.QUIZ_CORRECTED_NOT_NULL)
	private Long quizCorrect;
	
	@NotNull(message = MessageConstants.STUDENT_ID_NOT_NULL)
	private Long studentId;
	
	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EDITION_ID_NOT_EMPTY)
	private String editionId;
	
	@NotNull(message = MessageConstants.READING_LEVEL_NOT_NULL)
	@NotEmpty(message = MessageConstants.READING_LEVEL_NOT_EMPTY)
	private String readingLevel;
	
	private String genreId;

	@NotNull(message = MessageConstants.SCORE_YEAR_MONTH_NOT_NULL)
	@NotEmpty(message = MessageConstants.SCORE_YEAR_MONTH_NOT_EMPTY)
	private String scoreYearMonth;

}
