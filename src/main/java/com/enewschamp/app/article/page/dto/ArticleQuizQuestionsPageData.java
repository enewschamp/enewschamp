package com.enewschamp.app.article.page.dto;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleQuizQuestionsPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long newsArticleQuizId;
	private String question;
	private String opt1;
	private String opt2;
	private String opt3;
	private String opt4;
	@JsonInclude
	private int correctOpt;
	@JsonInclude
	private Long chosenOpt;

}
