package com.enewschamp.app.admin.reconciliation.dto;

import lombok.Data;

@Data
public class QuizInfo {
	private Long newsArticleQuizId;
	private Long response;
	private String responseCorrect;

}
