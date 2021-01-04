package com.enewschamp.app.article.page.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class NewsEventsPublicationData {

	private static final long serialVersionUID = 1L;

	@JsonInclude
	private Long newsArticleId;
	@JsonInclude
	private Long newsArticleGroupId;
	@JsonInclude
	private LocalDate publicationDate;
	@JsonInclude
	private Integer readingLevel;
	@JsonInclude
	private String genre;
	@JsonInclude
	private String headline;
	@JsonInclude
	private String city;
	@JsonInclude
	private String saved;
	@JsonInclude
	private String opinionText;
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
	@JsonInclude
	private String quizAvailable;
	@JsonInclude
	private String quizCompleted;
}
