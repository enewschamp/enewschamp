package com.enewschamp.app.admin.reconciliation.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class StudentDailyScoresDiscripancies {
	private List<DailyNewsArticleInfo> newsArticles;
	private Long studentId;
	private LocalDate date;
	private Integer readingLevel;
	private Long totalRecords;
	private Long totalCorrectResponse;
	private Long newsArticleCount;
	private Long quizAttempted;
	private Long quizCorrect;
	private Long articleRead;
}
