package com.enewschamp.article.app.dto;

import java.time.LocalDate;

import com.enewschamp.app.common.AbstractDTO;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.WeekDayType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class PublisherNewsArticleSummaryDTO extends AbstractDTO {

	private static final long serialVersionUID = -4590288574254659259L;
	@JsonInclude
	private Long newsArticleId;
	@JsonInclude
	private Long newsArticleGroupId;
	@JsonInclude
	private LocalDate publicationDate;
	@JsonInclude
	private Integer readingLevel;
	@JsonInclude
	private String authorId;
	@JsonInclude
	private String editorId;
	@JsonInclude
	private String publisherId;
	@JsonInclude
	private ArticleStatusType status;
	@JsonInclude
	private ArticleType articleType;
	@JsonInclude
	private String content;
	@JsonInclude
	private String rating;
	@JsonInclude
	private Long publicationId;
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
	private String authorWorked;
	@JsonInclude
	private String editorWorked;
	@JsonInclude
	private String publisherWorked;
	@JsonInclude
	private String genreId;
	@JsonInclude
	private String headline;
	@JsonInclude
	private String cityId;
	@JsonInclude
	private String credits;
	@JsonInclude
	private LocalDate intendedPubDate;
	@JsonInclude
	private MonthType intendedPubMonth;
	@JsonInclude
	private WeekDayType intendedPubDay;
	@JsonInclude
	private LocalDate targetCompletionDate;
	@JsonInclude
	private String url;
}
