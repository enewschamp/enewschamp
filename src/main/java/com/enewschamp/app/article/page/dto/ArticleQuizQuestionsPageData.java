package com.enewschamp.app.article.page.dto;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ArticleQuizQuestionsPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long newsArticleQuizId;
	private String question;
	private int optSeq1;
	private int optSeq2;
	private int optSeq3;
	private int optSeq4;
	private String opt1;
	private String opt2;
	private String opt3;
	private String opt4;
	private int correctOptSeq;
	private Long chosenOptSeq;
	
}
