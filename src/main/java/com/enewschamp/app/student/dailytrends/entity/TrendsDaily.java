package com.enewschamp.app.student.dailytrends.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "TrendsDaily", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "studentId", "editionId", "quizPublicationDate" }) })
@EqualsAndHashCode(callSuper = false)
public class TrendsDaily extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trendsDaily_id_generator")
	@SequenceGenerator(name = "trendsDaily_id_generator", sequenceName = "trendsDaily_seq", allocationSize = 1)
	@Column(name = "trendsDailyId", updatable = false, nullable = false)
	private Long trendsDailyId;

	@NotNull
	@Column(name = "studentId", length = 10)
	private Long studentId;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;

	@NotNull
	@Column(name = "quizPublicationDate")
	private LocalDate quizPublicationDate;

	@Column(name = "articlesRead", length = 2)
	private Long articlesRead;

	@Column(name = "quizAttempted", length = 2)
	private Long quizAttempted;

	@Column(name = "quizCorrect", length = 2)
	private Long quizCorrect;

	@Column(name = "readingLevel", length = 1)
	private int readingLevel;

}
