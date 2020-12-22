package com.enewschamp.app.article.page.dto;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleQuizAnswers extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long newsArticleQuizId;
	private Long chosenOpt;
}
