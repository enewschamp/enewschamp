package com.enewschamp.article.app.dto;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.article.domain.common.ArticleRatingType;
import com.enewschamp.article.domain.common.ArticleStatusType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsArticleDTO extends MaintenanceDTO {	

	private static final long serialVersionUID = 8445791416971982306L;

	private Long newsArticleId;
	
	@NotNull
	private long newsArticleGroupId;
	
	@NotNull
	private int readingLevel;
	
	@NotNull
	private ArticleStatusType status;
	
	private String content;
	
	private ArticleRatingType rating;
	
	private int likeLCount;
	
	private int likeHCount;
	
	private int likeOCount;
	
	private int likeWCount;
	
	private int likeSCount;

}