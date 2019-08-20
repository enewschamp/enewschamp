package com.enewschamp.app.student.dailytrends.dto;

import java.time.LocalDate;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TrendsDailyDTO extends AbstractDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Long trendsDailyId;
	
	private Long studentId;
	
	private String editionId;
	
	private LocalDate quizDate;
	
	
	private Long articleRead;
	
	private Long quizQAttempted;
	
	private Long quizQCorrect;
	
}
