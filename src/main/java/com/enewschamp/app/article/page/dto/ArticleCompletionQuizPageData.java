package com.enewschamp.app.article.page.dto;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.welcome.page.data.BadgeDetailsDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleCompletionQuizPageData extends PageData {

	private static final long serialVersionUID = 1L;

	private long newsArticleId;

	@JsonInclude
	private String quizCompletionMessage;

	@JsonInclude
	private String soundFile;

	@JsonInclude
	private BadgeDetailsDTO monthlyBadge;

	@JsonInclude
	private BadgeDetailsDTO genreBadge;
}
