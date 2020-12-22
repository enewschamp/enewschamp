package com.enewschamp.app.article.page.dto;

import java.time.LocalDate;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NewsArticlePageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long newsArticleId;
	private long newsArticleGroupId;
	@JsonInclude
	private LocalDate publicationDate;
	@JsonInclude
	private int readingLevel;
	@JsonInclude
	private String genre;
	@JsonInclude
	private String headline;
	@JsonInclude
	private String content;
	@JsonInclude
	private String author;
	@JsonInclude
	private String credits;
	@JsonInclude
	private String quizAvailable;
	@JsonInclude
	private String quizCompleted;
	@JsonInclude
	private Long quizScore;
	@JsonInclude
	private String opinionText;
	@JsonInclude
	private String saved;
	@JsonInclude
	private String reaction;
	@JsonInclude
	private Integer reactionHCount;
	@JsonInclude
	private Integer reactionLCount;
	@JsonInclude
	private Integer reactionOCount;
	@JsonInclude
	private Integer reactionSCount;
	@JsonInclude
	private Integer reactionWCount;
}
