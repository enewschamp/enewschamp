package com.enewschamp.app.student.monthlytrends.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TrendsMonthlyTotalDTO extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Long trendsMonthlyTotalId;
	
	private Long studentId;
	
	private String editionId;
	
	private String yearMonth;
	
	
	private String genreId;
	
	private Long articlesRead;
	
	
	private Long quizQAttempted;
	
	private Long quizQCorrect;
	
}
