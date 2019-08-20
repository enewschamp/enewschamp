package com.enewschamp.app.article.page.dto;

import lombok.Data;

@Data
public class ArticleQuizCompletionDTO implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String quizCompletionMessage;
	private String sounndFile;
	private String newBadge;
	private String badgeName;
	private String newGenreBadge;
	private String newGenreBadgeName;
	private Long articleId;
	
}
