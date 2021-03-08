package com.enewschamp.article.domain.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.beans.factory.annotation.Autowired;

import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.article.domain.common.ArticleActionType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.domain.common.StatusTransitionHandler;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "NewsArticle", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "newsArticleGroupId", "readingLevel" }) })
@JaversSpringDataAuditable
public class NewsArticle extends BaseEntity {

	private static final long serialVersionUID = 4067120832023693933L;

	@Autowired
	@Transient
	private StatusTransitionHandler stateTransitionHandler;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_article_id_generator")
	@SequenceGenerator(name = "news_article_id_generator", sequenceName = "news_article_id_seq", allocationSize = 1)
	@Column(name = "NewsArticleId", updatable = false, nullable = false)
	private Long newsArticleId;

	@NotNull
	@Column(name = "NewsArticleGroupId", length = 10)
	private Long newsArticleGroupId = 0L;

	@NotNull
	@Column(name = "ReadingLevel", length = 1)
	private Integer readingLevel = 0;

	@NotNull
	@Column(name = "Status")
	@Enumerated(EnumType.STRING)
	private ArticleStatusType status;

	@Column(name = "PreviousStatus")
	@Enumerated(EnumType.STRING)
	@DiffIgnore
	private ArticleStatusType previousStatus;

	@Column(name = "Content")
	@Lob
	private String content;

	@Column(name = "Rating")
	private String rating;

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

	@Column(name = "ReadyForTest", length = 1)
	private String readyForTest = "N";

	@Column(name = "PublicationDate")
	private LocalDate publicationDate;

	@Column(name = "PublisherWorked", length = ForeignKeyColumnLength.UserId)
	private String publisherWorked;

	@Column(name = "PublicationId")
	private Long publicationId;

	@Column(name = "Sequence")
	private int sequence;

	@Column(name = "EditorWorked", length = ForeignKeyColumnLength.UserId)
	private String editorWorked;

	@Column(name = "AuthorWorked", length = ForeignKeyColumnLength.UserId)
	private String authorWorked;

	@Enumerated(EnumType.STRING)
	@DiffIgnore
	private ArticleActionType currentAction;

	@Column(name = "Comments")
	@Lob
	private String comments;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "article_id")
	private List<NewsArticleQuiz> newsArticleQuiz;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "newsArticleId")
	private List<StudentActivity> studentActivities;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		super.prePersist();
		if (newsArticleQuiz != null && newsArticleId != null && newsArticleId != 0) {
			for (NewsArticleQuiz question : newsArticleQuiz) {
				question.setNewsArticleId(newsArticleId);
			}
		}
	}

	public String getKeyAsString() {
		return String.valueOf(this.newsArticleId);
	}

	public void setStatus(ArticleStatusType status, ArticleStatusType previousStatus) {
		this.previousStatus = (previousStatus == null ? status : previousStatus);
		this.status = status;
	}

}
