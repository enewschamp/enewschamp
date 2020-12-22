package com.enewschamp.article.app.dto;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.article.domain.common.ArticleActionType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.WeekDayType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Embeddable
@Data
@EqualsAndHashCode(callSuper = false)
public class NewsArticleDTO extends MaintenanceDTO {

	private static final long serialVersionUID = 8445791416971982306L;

	public NewsArticleDTO() {

	}

	public NewsArticleDTO(Long newsArticleId, Long newsArticleGroupId, int readingLevel) {
		this.newsArticleId = newsArticleId;
		this.newsArticleGroupId = newsArticleGroupId;
		this.readingLevel = readingLevel;
	}

	@JsonInclude
	private String editorId;

	@JsonInclude
	private String authorId;

	@JsonInclude
	private String headline;

	@JsonInclude
	private String credits;

	@JsonInclude
	private String url;

	@JsonInclude
	private String sourceText;

	@JsonInclude
	private String hashTags;

	@JsonInclude
	private boolean noQuiz;

	@JsonInclude
	private boolean imageOnly;

	@JsonInclude
	private String cityId;

	@JsonInclude
	private ArticleType articleType;

	@JsonInclude
	private String genreId;

	@JsonInclude
	private LocalDate targetCompletionDate;

	@JsonInclude
	private LocalDate intendedPubDate;

	@JsonInclude
	private MonthType intendedPubMonth;

	@JsonInclude
	private WeekDayType intendedPubDay;

	@JsonInclude
	private Long newsArticleId;

	@JsonInclude
	@NotNull
	private Long newsArticleGroupId;

	@JsonInclude
	@NotNull
	private Integer readingLevel;

	@JsonInclude
	@NotNull
	private ArticleStatusType status;

	@JsonInclude
	private String content;

	@JsonInclude
	private String rating;

	@JsonInclude
	private Integer likeLCount;

	@JsonInclude
	private Integer likeHCount;

	@JsonInclude
	private Integer likeOCount;

	@JsonInclude
	private Integer likeWCount;

	@JsonInclude
	private Integer likeSCount;

	@JsonInclude
	private LocalDate publicationDate;

	@JsonInclude
	private Long publicationId;

	@JsonInclude
	private int sequence;

	@JsonInclude
	private String editorWorked;

	@JsonInclude
	private String authorWorked;

	@JsonInclude
	private String publisherWorked;

	@JsonInclude
	private String comments;

	@JsonInclude
	private ArticleActionType currentAction;

	@JsonInclude
	private List<NewsArticleQuizDTO> newsArticleQuiz;
}
