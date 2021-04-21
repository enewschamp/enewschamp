package com.enewschamp.article.domain.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.javers.spring.annotation.JaversSpringDataAuditable;

import com.enewschamp.article.domain.common.ArticleGroupStatusType;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.WeekDayType;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "NewsArticleGroup")
@JaversSpringDataAuditable
public class NewsArticleGroup extends BaseEntity {

	private static final long serialVersionUID = 5585724931483394475L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_article_group_id_generator")
	@SequenceGenerator(name = "news_article_group_id_generator", sequenceName = "news_article_group_id_seq", allocationSize = 1)
	@Column(name = "NewsArticleGroupId", updatable = false, nullable = false)
	private Long newsArticleGroupId;

	@NotNull
	@Column(name = "ArticleType")
	@Enumerated(EnumType.STRING)
	private ArticleType articleType;

	@NotNull
	@Column(name = "EditionId", length = ForeignKeyColumnLength.EditionId)
	private String editionId;

	@Column(name = "CityId", length = ForeignKeyColumnLength.CityId)
	private String cityId;

	@NotNull
	@Column(name = "GenreId", length = ForeignKeyColumnLength.GenreId)
	private String genreId;

	@NotNull
	@Column(name = "EditorId", length = ForeignKeyColumnLength.UserId)
	private String editorId;

	@NotNull
	@Column(name = "Headline", length = 100)
	private String headline;

	@NotNull
	@Column(name = "Credits", length = 200)
	private String credits;

	@NotNull
	@Column(name = "Status", length = 25)
	@Enumerated(EnumType.STRING)
	private ArticleGroupStatusType status;

	@Column(name = "URL", length = 200)
	private String url;

	@Column(name = "SourceText")
	@Lob
	private String sourceText;

	@NotNull
	@Column(name = "HashTags")
	@Lob
	private String hashTags;

	@Column(name = "NoQuiz", length = 1)
	private String noQuiz;

	@Column(name = "ImageOnly", length = 1)
	private String imageOnly;

	@Column(name = "ImageName", length = 100)
	private String imageName;

	@Transient
	private String base64Image;

	@Transient
	private String imageTypeExt;

	@Column(name = "TextCoordinateX", length = 10)
	private String textCoordinateX;

	@Column(name = "TextCoordinateY", length = 10)
	private String textCoordinateY;

	@Column(name = "TextBoxWidth", length = 10)
	private String textBoxWidth;

	@Column(name = "TextBoxLength", length = 10)
	private String textBoxLength;

	@Column(name = "AuthorId", length = ForeignKeyColumnLength.UserId)
	private String authorId;

	@Column(name = "TargetCompletionDate")
	private LocalDate targetCompletionDate;

	@Column(name = "IntendedPubDate")
	private LocalDate intendedPubDate;

	@Column(name = "IntendedPubMonth", length = 3)
	@Enumerated(EnumType.STRING)
	private MonthType intendedPubMonth;

	@Column(name = "IntendedPubDay", length = 3)
	@Enumerated(EnumType.STRING)
	private WeekDayType intendedPubDay;

	@Column(name = "Comments")
	@Lob
	private String comments;

	@Column(name = "ReadingLevel1")
	@NotNull
	private Boolean readingLevel1;

	@Column(name = "ReadingLevel2")
	@NotNull
	private Boolean readingLevel2;

	@Column(name = "ReadingLevel3")
	@NotNull
	private Boolean readingLevel3;

	@Column(name = "ReadingLevel4")
	@NotNull
	private Boolean readingLevel4;

	@Transient
	private String deleteImage;

	@Transient
	private List<NewsArticle> newsArticles;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		super.prePersist();
	}

	public String getKeyAsString() {
		return String.valueOf(this.newsArticleGroupId);
	}
}