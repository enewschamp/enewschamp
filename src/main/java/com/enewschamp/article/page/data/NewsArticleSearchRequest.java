package com.enewschamp.article.page.data;

import java.time.LocalDate;
import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.domain.common.MonthType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
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
	private LocalDate publicationDateFrom;
	private LocalDate publicationDateTo;
	private LocalDate publicationDate;
	private MonthType intendedPubMonth;
	private List<ArticleStatusType> articleStatusList;
	private List<ArticleType> articleTypeList;
	private String city;

}
