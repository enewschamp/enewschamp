package com.enewschamp.app.trends.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Table(name = "monthly_articles_vw")
@Entity
@Immutable
public class MonthlyArticles {

	@Id
	@Column(name = "month")
	private String month;

	@Column(name = "articlesRead")
	private Long articlesRead;

	@Column(name = "articlesPublished")
	private Long articlesPublished;

}
