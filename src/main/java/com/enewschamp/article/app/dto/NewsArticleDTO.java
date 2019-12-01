package com.enewschamp.article.app.dto;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.article.domain.common.ArticleActionType;
import com.enewschamp.article.domain.common.ArticleRatingType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.common.ArticleType;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	private Long newsArticleId;
	
	@NotNull
	private Long newsArticleGroupId;
	
	@NotNull
	private Integer readingLevel;
	
	@NotNull
	private ArticleStatusType status;
	
	private ArticleType articleType = ArticleType.NEWSARTICLE;
	
	private String content;
	
	private ArticleRatingType rating;
	
	private Integer likeLCount;
	
	private Integer likeHCount;
	
	private Integer likeOCount;
	
	private Integer likeWCount;
	
	private Integer likeSCount;

	private LocalDate publishDate;
	
	private String publisherId;

	private Long publicationId;
	
	private String editorId;
	
	private String authorId;
	
	@NotNull
	private ArticleActionType currentAction;
	
	private List<NewsArticleQuizDTO> newsArticleQuiz;
	
	// Below attribute added for state transition specific validations .e.g. comments are mandatory for ReWork related statuses
	@Transient
	@JsonIgnore
	private String currentComments;
}
