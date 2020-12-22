package com.enewschamp.app.trends.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Table(name = "monthly_quiz_score_vw")
@Entity
@Immutable
public class MonthlyQuizScores {

	@Id
	@Column(name = "month")
	private String month;

	@Column(name = "quizPublished")
	private Long quizPublished;

	@Column(name = "quizAttempted")
	private Long quizAttempted;

	@Column(name = "quizCorrect")
	private Long quizCorrect;
}
