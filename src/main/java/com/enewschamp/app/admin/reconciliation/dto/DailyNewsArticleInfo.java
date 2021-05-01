package com.enewschamp.app.admin.reconciliation.dto;

import java.util.List;

import lombok.Data;

@Data
public class DailyNewsArticleInfo {
	private Long newsArticleId;
	private List<QuizInfo> quizInfoList;
}
