package com.enewschamp.article.domain.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.article.domain.common.ArticleRatingType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="NewsArticle")
public class NewsArticle extends BaseEntity {	
	
	private static final long serialVersionUID = 4067120832023693933L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_id_generator")
	@SequenceGenerator(name="article_id_generator", sequenceName = "art_id_seq", allocationSize=1)
	@Column(name = "NewsArticleID", updatable = false, nullable = false)
	private Long newsArticleId;
	
	@NotNull
	@Column(name = "NewsArticleGroupID", length=10)
	private long newsArticleGroupId = 0L;
	
	@NotNull
	@Column(name = "ReadingLevel")
	private int readingLevel = 0;
	
	@NotNull
	@Column(name = "Status")
	@Enumerated(EnumType.STRING)
	private ArticleStatusType status;
	
	@Column(name = "Content")
	@Lob
	private String content;
	
	@Column(name = "Rating")
	@Enumerated(EnumType.STRING)
	private ArticleRatingType rating;
	
	@Column(name = "LikeLCount")
	private int likeLCount = 0;
	
	@Column(name = "LikeHCount")
	private int likeHCount = 0;
	
	@Column(name = "LikeOCount")
	private int likeOCount = 0;
	
	@Column(name = "LikeWCount")
	private int likeWCount = 0;
	
	@Column(name = "LikeSCount")
	private int likeSCount = 0;

	@Column(name = "PublishDate")
	private LocalDate publishDate;
	
	@Column(name = "PublisherId", length = ForeignKeyColumnLength.UserId)
	private String publisherId;

	@Column(name = "PublicationId")
	private long publicationId;
	
	@Column(name = "EditorId", length = ForeignKeyColumnLength.UserId)
	private String editorId;
	
	@Column(name = "AuthorId", length = ForeignKeyColumnLength.UserId)
	private String authorId;
	
}
