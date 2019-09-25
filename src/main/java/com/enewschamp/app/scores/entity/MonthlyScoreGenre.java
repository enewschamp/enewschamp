package com.enewschamp.app.scores.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Table(name = "monthly_score_genre_vw")
@Entity
@Immutable
public class MonthlyScoreGenre implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	private String genreid;
	private Long articlesPublished;
	private Long articlesRead;
	private Long quizPublished;
	private Long quizAttempted;
	private Long quizCorrect;
	
	
}
