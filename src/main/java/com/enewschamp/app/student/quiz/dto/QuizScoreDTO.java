package com.enewschamp.app.student.quiz.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QuizScoreDTO extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long quizScoreId;

	private Long studentId;

	private Long newsArticleQuizId;

	private Long response;

	private String responseCorrect;

}
