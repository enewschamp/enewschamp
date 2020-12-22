package com.enewschamp.app.trends.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Table(name = "monthly_articles_genre_vw")
@Entity
@Immutable
public class MonthlyArticlesGenre {

	@Id
	@Column(name = "genreId")
	private String genreId;

	@Column(name = "articlesRead")
	private Long articlesRead;

	@Column(name = "articlesPublished")
	private Long articlesPublished;

}
