package com.enewschamp.app.scores.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Data;


@Data
@Table(name = "monthly_articles_genre_vw")
@Entity
@Immutable
public class DailyScore implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "publishDate", updatable = false, nullable = false)
	private LocalDate publishDate;
	
	//@Column(name="month")
	//private Integer month;
	
	@Column(name="articlesPublished")
	private Long articlesPublished;
	
	@Column(name="articlesRead")
	private Long articlesRead;
	
	@Column(name="quizPublished")
	private Long quizPublished;
	
	@Column(name="quizAttempted")
	private Long quizAttempted;
	
	@Column(name="quizCorrect")
	private Long quizCorrect;
	
}
