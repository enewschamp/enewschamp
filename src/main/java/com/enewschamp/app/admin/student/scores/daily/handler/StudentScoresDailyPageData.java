package com.enewschamp.app.admin.student.scores.daily.handler;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentScoresDailyPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long scoresDailyId;

	@NotNull(message = MessageConstants.PUBLICATION_DATE_NOT_NULL)
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
	@NotNull(message = MessageConstants.EDITION_ID_NOT_EMPTY)
	private String editionId;

	@NotNull(message = MessageConstants.READING_LEVEL_NOT_NULL)
	@NotEmpty(message = MessageConstants.READING_LEVEL_NOT_EMPTY)
	private String readingLevel;

}
