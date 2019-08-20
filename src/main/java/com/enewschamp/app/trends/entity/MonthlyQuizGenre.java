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
	@Column(name = "publishDate", updatable = false, nullable = false)
	private Integer publishDate;
	
	
	@Column(name = "quizpublished")
	private Long quizpublished;

	@Column(name = "quizAttempted")
	private Long quizAttempted;
	
	@Column(name = "quizcorrect")
	private Long quizcorrect;
	
	@Column(name = "genreid")
	private String genreid;
	
}
