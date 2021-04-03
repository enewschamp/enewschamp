package com.enewschamp.app.article.page.dto;

import com.enewschamp.app.welcome.page.data.BadgeDetailsDTO;

import lombok.Data;

@Data
public class ArticleQuizCompletionDTO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long articleId;
	private String quizCompletionMessage;
	private String soundFile;
	private BadgeDetailsDTO monthlyBadge;
	private BadgeDetailsDTO genreBadge;
}