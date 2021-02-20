package com.enewschamp.app.admin.article.monthly;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Table(name = "monthly_published_articles_genre_vw")
@Entity
@Immutable
@IdClass(PublicationMonthly.class)
public class ArticlePublicationMonthlyGenre {
	
	@Id
	@NotNull
	@Column(name = "YearMonth", updatable = false, nullable = false)
	private String yearMonth;

	@Id
	@NotNull
	@Column(name = "ReadingLevel", updatable = false, nullable = false)
	private String readingLevel;
	
	@Id
	@NotNull
	@Column(name = "GenreId")
	private String genreId;
	
	@NotNull
	@Column(name = "QuizPublished")
	private Integer quizPublished = 0;

	@NotNull
	@Column(name = "ArticlesPublished")
	private Integer articlesPublished = 0;

}
