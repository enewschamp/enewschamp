package com.enewschamp.app.admin.reconciliation.dto;

import lombok.Data;

@Data
public class StudentInfo {
	private int quizScoreTotal;
	private int studentActivityQuizScore;
	private QuizInfo quizInfo;

}
