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

import com.enewschamp.article.domain.common.ArticleGroupStatusType;
import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.WeekDayType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="NewsArticleGroup")
public class NewsArticleGroup extends BaseEntity {	
	
	private static final long serialVersionUID = 5585724931483394475L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_group_id_generator")
	@SequenceGenerator(name="article_group_id_generator", sequenceName = "art_grp_id_seq", allocationSize=1)
	@Column(name = "NewsArticleGroupID", updatable = false, nullable = false)
	private Long newsArticleGroupId;
	
	@NotNull
	@Column(name = "EditionID", length = ForeignKeyColumnLength.EditionId)
	private String editionId;
	
	@NotNull
	@Column(name = "GenreID", length = ForeignKeyColumnLength.GenreId)
	private String genreId;
	
	@NotNull
	@Column(name = "Headline", length = 100)
	private String headline;
	
	@NotNull
	@Column(name = "Credits", length = 200)
	private String credits;
	
	@NotNull
	@Column(name = "Status", length= 25)
	@Enumerated(EnumType.STRING)
	private ArticleGroupStatusType status;
	
	@Column(name = "URL", length = 200)
	private String url;
	
	@Column(name = "SourceText")
	@Lob
	private String sourceText;
	
	@Column(name = "HashTags")
	@Lob
	private String hashTags;
	
	@Column(name = "ImagePathMobile", length = 200)
	private String imagePathMobile;
	
	@Column(name = "ImagePathTab", length = 200)
	private String imagePathTab;
	
	@Column(name = "ImagePathDesktop", length = 200)
	private String imagePathDesktop;
	
	@Column(name = "EditorId", length = ForeignKeyColumnLength.UserId)
	private String editorId;
	
	@Column(name = "AuthorId", length = ForeignKeyColumnLength.UserId)
	private String authorId;
	
	@Column(name = "TargetCompletionDate")
	private LocalDate targetCompletionDate;
	
	@Column(name = "PublicationDate")
	private LocalDate publicationDate;
	
	@Column(name = "IntendedPubMonth")
	private MonthType intendedPubMonth;
	
	@Column(name = "IntendedPubDay", length=3)
	@Enumerated(EnumType.STRING)
	private WeekDayType intendedPubDay;
	
	@Column(name = "Comments")
	@Lob
	private String comments;
		
}
