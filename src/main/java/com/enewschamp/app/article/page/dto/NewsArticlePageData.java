package com.enewschamp.app.article.page.dto;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsArticlePageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long newsArticleId;
	private String authorId;
	private String premiumSubMsg;
	private String image;
	private String genreId;
	private String headline;
	private String content;
	private String credits;
	private String reactionType;
	private String opitionText;
	private String saved;
	private boolean quizCompletedIndicator;
	
	
	
	
}
