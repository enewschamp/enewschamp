package com.enewschamp.app.student.monthlytrends.entity;

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
@Table(name="TrendsMonthlyTotal",uniqueConstraints={@UniqueConstraint(columnNames = {"studentId" , "editionId","trendyearMonth"})})
@EqualsAndHashCode(callSuper=false)
public class TrendsMonthlyTotal extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trendsMonthlyTotal_id_generator")
	@SequenceGenerator(name="trendsMonthlyTotal_id_generator", sequenceName = "trendsMonthlyTotal_seq", allocationSize=1)
	@Column(name = "trendsMonthlyTotalId", updatable = false, nullable = false)
	private Long trendsMonthlyTotalId;
	
	
	@NotNull
	@Column(name = "studentId", length=10)
	private Long studentId;
	
	@NotNull
	@Column(name = "editionId", length=6)
	private String editionId;
	
	@Column(name = "trendyearMonth")
	private Long yearMonth;
	
	
	@Column(name = "genreId", length=12)
	private String genreId;
	
	@Column(name = "articlesRead", length=3)
	private Long articlesRead;
	
	
	@Column(name = "quizAttempted", length=2)
	private Long quizQAttempted;
	
	@Column(name = "quizCorrect", length=2)
	private Long quizQCorrect;
	
	
	
	
}
