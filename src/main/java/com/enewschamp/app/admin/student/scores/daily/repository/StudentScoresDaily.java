package com.enewschamp.app.admin.student.scores.daily.repository;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "ScoresDaily")
@EqualsAndHashCode(callSuper = false)
public class StudentScoresDaily extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scores_daily_id_generator")
	@SequenceGenerator(name = "scores_daily_id_generator", sequenceName = "scores_daily_id_seq", allocationSize = 1)
	@Column(name = "scoresDailyId", length = 3)
	private Long scoresDailyId;
	
	@Column(name = "publicationDate")
	private LocalDate publicationDate;

	@Column(name = "articlesRead")
	private Long articlesRead;

	@Column(name = "quizAttempted")
	private Long quizAttempted;

	@Column(name = "quizCorrect")
	private Long quizCorrect;
	
	@Column(name = "studentId")
	private Long studentId;
	
	@Column(name = "editionId")
	private String editionId;
	
	@Column(name = "readingLevel")
	private String readingLevel;

}
