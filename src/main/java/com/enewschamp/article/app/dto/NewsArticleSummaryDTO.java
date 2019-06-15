package com.enewschamp.article.app.dto;

import java.time.LocalDate;

import com.enewschamp.app.common.AbstractDTO;
import com.enewschamp.article.domain.common.ArticleStatusType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
public class NewsArticleSummaryDTO extends AbstractDTO {	

	private static final long serialVersionUID = -4590288574254659259L;

	private Long newsArticleId;
	private Long newsArticleGroupId;
	private LocalDate publicationDate;
	private Integer readingLevel;
	private String authorId;
	private String editorId;
	private String publisherId;
	private ArticleStatusType status;
	private String genreId;
	private String headLine;
	private String imagePathMobile;
	private String imagePathTab;
	private String imagePathDesktop;
}
