package com.enewschamp.article.domain.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import com.enewschamp.article.domain.common.ArticleActionType;
import com.enewschamp.article.domain.common.ArticleRatingType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.domain.common.StatusTransitionHandler;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="NewsArticle",
	   uniqueConstraints= {
	    @UniqueConstraint(columnNames = {"newsArticleGroupId", "readingLevel"})
	})
public class NewsArticle extends BaseEntity {	
	
	private static final long serialVersionUID = 4067120832023693933L;
	
	@Autowired
	@Transient
	private StatusTransitionHandler stateTransitionHandler;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_id_generator")
	@SequenceGenerator(name="article_id_generator", sequenceName = "art_id_seq", allocationSize=1)
	@Column(name = "NewsArticleID", updatable = false, nullable = false)
	private Long newsArticleId;
	
	@NotNull
	@Column(name = "NewsArticleGroupID", length=10)
	private Long newsArticleGroupId = 0L;
	
	@NotNull
	@Column(name = "ReadingLevel")
	private Integer readingLevel = 0;
	
	@NotNull
	@Column(name = "Status")
	@Enumerated(EnumType.STRING)
	private ArticleStatusType status = ArticleStatusType.Unassigned;
	
	@Column(name = "Content")
	@Lob
	private String content;
	
	@Column(name = "Rating")
	@Enumerated(EnumType.STRING)
	private ArticleRatingType rating;
	
	@Column(name = "LikeLCount")
	private Integer likeLCount = 0;
	
	@Column(name = "LikeHCount")
	private Integer likeHCount = 0;
	
	@Column(name = "LikeOCount")
	private Integer likeOCount = 0;
	
	@Column(name = "LikeWCount")
	private Integer likeWCount = 0;
	
	@Column(name = "LikeSCount")
	private Integer likeSCount = 0;

	@Column(name = "PublishDate")
	private LocalDate publishDate;
	
	@Column(name = "PublisherId", length = ForeignKeyColumnLength.UserId)
	private String publisherId;

	@Column(name = "PublicationId")
	private Long publicationId;
	
	@Column(name = "EditorId", length = ForeignKeyColumnLength.UserId)
	private String editorId;
	
	@Column(name = "AuthorId", length = ForeignKeyColumnLength.UserId)
	private String authorId;
	
	@Enumerated(EnumType.STRING)
	@DiffIgnore
	private ArticleActionType currentAction;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "article_Id")
	private List<NewsArticleQuiz> newsArticleQuiz;
	
	@PrePersist
	@PreUpdate
	public void prePersist() {
		super.prePersist();
		if(newsArticleQuiz != null && newsArticleId != 0) {
			for(NewsArticleQuiz question: newsArticleQuiz) {
				question.setNewsArticleId(newsArticleId);
			}
		}
	}
	
	public String getKeyAsString() {
		return String.valueOf(this.newsArticleId);
	}
	
}
