package com.enewschamp.app.student.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentActivityDTO extends AbstractDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentActivityId;
	
	private Long studentId;
	
	private Long NewsArticleId;
	
	private String saved;
	
	private String likeLevel;
	
	private String opinion;

	private Long quizScore;
	
}
