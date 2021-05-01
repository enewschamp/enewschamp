package com.enewschamp.app.admin.student.activity.handler;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentActivityPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long studentActivityId;
	private Long studentId;
	private Long newsArticleId;
	private String saved;
	private String reaction;
	private String opinion;
	private Long quizScore;
	private String editionId;
	private int readingLevel;
}
