package com.enewschamp.article.app.dto;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.enewschamp.app.common.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Entity
public class NewsArticleViewDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	@Id
	@JsonInclude
	private Long newsArticleId;
	@JsonInclude
	private Long newsArticleGroupId;
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
	private String city;
	@JsonInclude
	private String imageName;
}