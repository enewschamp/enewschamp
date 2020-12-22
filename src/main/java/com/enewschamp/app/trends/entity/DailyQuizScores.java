package com.enewschamp.app.trends.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Table(name = "daily_quiz_scores_vw")
@Entity
@Immutable
public class DailyQuizScores {

	@Id
	@Column(name = "publicationDate", updatable = false, nullable = false)
	private LocalDate publicationDate;

	@Column(name = "quizPublished")
	private Long quizPublished;

	@Column(name = "quizAttempted")
	private Long quizAttempted;

	@Column(name = "quizCorrect")
	private Long quizCorrect;
}
