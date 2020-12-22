package com.enewschamp.app.trends.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Table(name = "daily_article_vw")
@Entity
@Immutable
public class DailyArticle {

	@Id
	@Column(name = "publicationDate", updatable = false, nullable = false)
	private LocalDate publicationDate;

	@Column(name = "articlesRead")
	private Long articlesRead;

	@Column(name = "articlesPublished")
	private Long articlesPublished;

}
