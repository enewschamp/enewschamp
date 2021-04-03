package com.enewschamp.publication.app.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;
import com.enewschamp.publication.domain.common.PublicationActionType;
import com.enewschamp.publication.domain.common.PublicationStatusType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -1938738416579485109L;

	@JsonInclude
	private Long publicationId;

	@JsonInclude
	@NotNull
	private Long publicationGroupId;

	@JsonInclude
	@NotNull
	private int readingLevel;

	@JsonInclude
	@NotNull
	private LocalDate publicationDate;

	@JsonInclude
	@NotNull
	private String readyForTest;

	@JsonInclude
	private PublicationStatusType status;

	@JsonInclude
	@Size(max = ForeignKeyColumnLength.UserId)
	private String editorWorked;

	@JsonInclude
	@Size(max = ForeignKeyColumnLength.UserId)
	private String publisherWorked;

	private String comments;

	@JsonInclude
	private String rating;

	@JsonInclude
	@NotNull
	private PublicationActionType currentAction;

	@JsonInclude
	private List<NewsArticleDTO> newsArticles;

	private List<NewsArticleDTO> newsArticlesLinked;

	public PublicationDTO() {
		super();
	}

	public PublicationDTO(Long publicationId, Long publicationGroupId, String editionId, int readingLevel,
			LocalDate publicationDate, PublicationStatusType status, String editorWorked, String publisherWorked) {
		super();
		this.publicationId = publicationId;
		this.publicationGroupId = publicationGroupId;
		this.readingLevel = readingLevel;
		this.publicationDate = publicationDate;
		this.status = status;
		this.editorWorked = editorWorked;
		this.publisherWorked = publisherWorked;
	}

}