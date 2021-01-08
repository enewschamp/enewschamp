package com.enewschamp.app.student.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentActivityDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentActivityId;

	private Long studentId;

	private Long newsArticleId;

	private String editionId;

	private Long readingLevel;

	private String saved;

	private String reaction;

	private String opinion;

	private Long quizScore;

}
