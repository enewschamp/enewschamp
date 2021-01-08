package com.enewschamp.article.page.data;

import java.time.LocalDate;
import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.domain.common.YesNoType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsArticleSearchRequest extends PageData {

	private static final long serialVersionUID = -3606449396790568509L;

	private String userId;
	private String authorId;
	private String editorId;
	private String publisherId;
	private String editionId;
	private String genreId;
	private String headline;
	private String credits;
	private Long articleId;
	private Long articleGroupId;
	private String readingLevel1;
	private String readingLevel2;
	private String readingLevel3;
	private String readingLevel4;
	private LocalDate publicationDate;
	private LocalDate publicationDateFrom;
	private LocalDate publicationDateTo;
	private LocalDate publicationDateLimit;
	private LocalDate intendedPubDateFrom;
	private LocalDate intendedPubDateTo;
	private MonthType intendedPubMonth;
	private WeekDayType intendedPubDay;
	private ArticleType articleType;
	private YesNoType isLinked;
	private String cityId;
	private List<ArticleStatusType> status;
	private String isTestUser;
}
