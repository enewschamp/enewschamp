package com.enewschamp.app.scores.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ScoresMonthlyDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String month;
	private Long articlesPublished;
	private Long articlesRead;
	private Long quizPublished;
	private Long quizAttempted;
	private Long quizCorrect;
}
