package com.enewschamp.article.app.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.article.domain.common.ArticleGroupStatusType;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsArticleGroupDTO extends MaintenanceDTO {	
	
	private static final long serialVersionUID = -1650358898532968558L;

	private long newsArticleGroupId;
	
	@NotNull
	@Size(max=ForeignKeyColumnLength.EditionId)
	private String editionId;
	
	@NotNull
	@Size(max=ForeignKeyColumnLength.GenreId)
	private String genreId;
	
	@NotNull
	@Size(max=100)
	private String headline;
	
	@NotNull
	@Size(max=200)
	private String credits;
	
	@NotNull
	private ArticleGroupStatusType status;
	
	@Size(max=200)
	private String url;
	
	private String sourceText;
	
	private String hashTags;
	
	@Size(max=200)
	private String imagePathMobile;
	
	@Size(max=200)
	private String imagePathTab;
	
	@Size(max=200)
	private String imagePathDesktop;
	
	@Size(max = ForeignKeyColumnLength.UserId)
	private String editorId;
	
	@Size(max = ForeignKeyColumnLength.UserId)
	private String authorId;
	
	private Date targetCompletionDate;
	
	private Date publicationDate;
	
	private MonthType intendedPubMonth;
	
	private WeekDayType intendedPubDay;
	
	private String comments;
	
	private List<NewsArticleDTO> newsArticles;
}
