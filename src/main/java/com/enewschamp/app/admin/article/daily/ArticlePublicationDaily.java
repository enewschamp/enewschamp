package com.enewschamp.app.admin.article.daily;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Table(name = "daily_published_articles_vw")
@Entity
@Immutable
@IdClass(PublicationDaily.class)
public class ArticlePublicationDaily {

	@Id
	@NotNull
	@Column(name = "PublicationDate", updatable = false, nullable = false)
	private LocalDate publicationDate;

	@Id
	@NotNull
	@Column(name = "ReadingLevel", updatable = false, nullable = false)
	private String readingLevel;

	@NotNull
	@Column(name = "QuizPublished")
	private Integer quizPublished = 0;

	@NotNull
	@Column(name = "ArticlesPublished")
	private Integer articlesPublished = 0;

}
