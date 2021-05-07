package com.enewschamp.app.admin.student.quiz.handler;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QuizScorePageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long quizScoreId;
	private Long studentId;
	private Long newsArticleQuizId;
	private Long response;
	private String responseCorrect;
}
