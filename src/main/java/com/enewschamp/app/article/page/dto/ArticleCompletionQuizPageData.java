package com.enewschamp.app.article.page.dto;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ArticleCompletionQuizPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long newsArticleId;
	private String quizCompletionMessage;
	private String soundFile;
	private String newBadge;
	private String newBadgeName;
	private String newGenreBadge;
	private String newGenreBadgeName;
}
