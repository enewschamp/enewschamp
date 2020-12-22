package com.enewschamp.article.app.dto;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.article.domain.common.ArticleGroupStatusType;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NewsArticleGroupDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -1650358898532968558L;

	private Long newsArticleGroupId;

	@JsonInclude
	@NotNull
	@Size(max = ForeignKeyColumnLength.EditionId)
	private String editionId;

	@NotNull
	@Size(max = ForeignKeyColumnLength.GenreId)
	private String genreId;

	@JsonInclude
	@NotNull
	@Column(name = "ArticleType")
	@Enumerated(EnumType.STRING)
	private ArticleType articleType;

	@JsonInclude
	@Size(max = ForeignKeyColumnLength.CityId)
	private String cityId;

	@JsonInclude
	@NotNull
	@Size(max = 100)
	private String headline;

	@JsonInclude
	@NotNull
	@Size(max = 200)
	private String credits;

	@JsonInclude
	@NotNull
	private ArticleGroupStatusType status;

	@JsonInclude
	@Size(max = 200)
	private String url;

	@JsonInclude
	private String sourceText;

	@JsonInclude
	private String hashTags;

	@JsonInclude
	private boolean noQuiz;

	@JsonInclude
	private boolean imageOnly;

	@JsonInclude
	private String imageName;

	private String base64Image;

	@JsonInclude
	@Size(max = ForeignKeyColumnLength.UserId)
	private String editorId;

	@JsonInclude
	@Size(max = ForeignKeyColumnLength.UserId)
	private String authorId;

	@JsonInclude
	private LocalDate targetCompletionDate;

	@JsonInclude
	private LocalDate intendedPubDate;

	@JsonInclude
	private MonthType intendedPubMonth;

	@JsonInclude
	private WeekDayType intendedPubDay;

	@JsonInclude
	private String comments;

	@JsonInclude
	private Boolean readingLevel1;

	@JsonInclude
	private Boolean readingLevel2;

	@JsonInclude
	private Boolean readingLevel3;

	@JsonInclude
	private Boolean readingLevel4;

	@JsonInclude
	@Size(max = 10)
	private String textCoordinateX;

	@JsonInclude
	@Size(max = 10)
	private String textCoordinateY;

	@JsonInclude
	@Size(max = 10)
	private String textBoxWidth;

	@JsonInclude
	@Size(max = 10)
	private String textBoxLength;

	@JsonInclude
	private List<NewsArticleDTO> newsArticles;

	public boolean isReadingLevelPresent(int readingLevel) {
		boolean isPresent = false;
		switch (readingLevel) {
		case 1:
			isPresent = this.readingLevel1 != null ? this.readingLevel1 : false;
			break;
		case 2:
			isPresent = this.readingLevel2 != null ? this.readingLevel2 : false;
			break;
		case 3:
			isPresent = this.readingLevel3 != null ? this.readingLevel3 : false;
			break;
		case 4:
			isPresent = this.readingLevel4 != null ? this.readingLevel4 : false;
			break;
		}
		return isPresent;
	}

}
