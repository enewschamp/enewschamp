package com.enewschamp.app.admin.reconciliation.dto;

import java.util.List;

import lombok.Data;

@Data
public class NewsArticleInfo {
	private Long newsArticleId;
	private Long quizScoreTotal;
	private Long studentActivityQuizScore;
	private List<QuizInfo> quizInfoList;
}
