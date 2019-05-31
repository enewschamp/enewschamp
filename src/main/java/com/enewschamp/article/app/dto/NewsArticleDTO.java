package com.enewschamp.article.app.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.article.domain.common.ArticleRatingType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.publication.app.dto.PublicationArticleLinkageDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsArticleDTO extends MaintenanceDTO {	

	private static final long serialVersionUID = 8445791416971982306L;

	public NewsArticleDTO() {
		
	}
	
	public NewsArticleDTO(Long newsArticleId, Long newsArticleGroupId, int readingLevel) {
		this.newsArticleId = newsArticleId;
		this.newsArticleGroupId = newsArticleGroupId;
		this.readingLevel = readingLevel;
	}
	
	private long newsArticleId;
	
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

	private LocalDate publishDate;
	
	private String publisherId;

	private long publicationId;
	
	private String editorId;
	
	private String authorId;
	
	private List<NewsArticleQuizDTO> newsArticleQuiz;
	
}
