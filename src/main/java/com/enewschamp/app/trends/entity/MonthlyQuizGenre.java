package com.enewschamp.app.trends.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Table(name = "monthly_quiz_genre_vw")
@Entity
@Immutable
public class MonthlyQuizGenre {

	@Id
	@Column(name = "genreId")
	private String genreId;

	@Column(name = "quizPublished")
	private Long quizPublished;

	@Column(name = "quizAttempted")
	private Long quizAttempted;

	@Column(name = "quizCorrect")
	private Long quizCorrect;
}
