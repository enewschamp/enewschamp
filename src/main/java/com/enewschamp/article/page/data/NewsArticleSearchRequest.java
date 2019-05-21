package com.enewschamp.article.page.data;

import java.sql.Date;
import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.article.domain.common.ArticleStatusType;
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
	
	private String articleId;
	private String articleGroupId;
	private String readingLevel1;
	private String readingLevel2;
	private String readingLevel3;
	private String readingLevel4;
	private Date publicationDateFrom;
	private Date publicationDateTo;
	private Date publicationDate;
	private MonthType intendedPubMonth;
	private List<ArticleStatusType> articleStatusList;

}
