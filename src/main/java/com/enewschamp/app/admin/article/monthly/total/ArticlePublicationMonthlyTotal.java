package com.enewschamp.app.admin.article.monthly.total;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Table(name = "monthly_published_articles_total_vw")
@Entity
@Immutable
@IdClass(PublicationMonthlyTotal.class)
public class ArticlePublicationMonthlyTotal {

	@Id
	@NotNull
	@Column(name = "YearMonth", updatable = false, nullable = false)
	private String yearMonth;

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
