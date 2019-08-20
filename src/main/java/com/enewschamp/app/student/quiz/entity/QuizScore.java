package com.enewschamp.app.student.quiz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name="QuizScore")
@EqualsAndHashCode(callSuper=false)
public class QuizScore extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "StudentActivity_id_generator")
	@SequenceGenerator(name="quizScoreId_id_generator", sequenceName = "studentActivity_seq", allocationSize=1)
	@Column(name = "quizScoreId", updatable = false, nullable = false)
	private Long quizScoreId;
	
	@NotNull
	@Column(name = "studentId", length=10)
	private Long studentId;
	
	@NotNull
	@Column(name = "NewsArticleQuizId", length=10)
	private Long newsArticleQuizId;
	
	@Column(name = "response", length=1)
	private Long response;
	
	@Column(name = "responseCorrect", length=1)
	private String responseCorrect;
	
	
	
	
}
