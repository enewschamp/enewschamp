package com.enewschamp.article.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.DiffIgnore;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "NewsArticleQuiz")
public class NewsArticleQuiz extends BaseEntity {

	private static final long serialVersionUID = -4602954641558653630L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_id_generator")
	@SequenceGenerator(name = "quiz_id_generator", sequenceName = "art_quiz_id_seq", allocationSize = 1)
	@Column(name = "NewsArticleQuizId", updatable = false, nullable = false)
	private long newsArticleQuizId = 0L;

	@NotNull
	@Column(name = "NewsArticleId", length = 10)
	@DiffIgnore
	private long newsArticleId = 0L;

	@NotNull
	@Column(name = "Question", length = 99)
	private String question;

	@NotNull
	@Column(name = "Opt1", length = 99)
	private String opt1;

	@NotNull
	@Column(name = "Opt2", length = 99)
	private String opt2;

	@Column(name = "Opt3", length = 99)
	private String opt3;

	@Column(name = "Opt4", length = 99)
	private String opt4;

	@NotNull
	@Column(name = "CorrectOpt", length = 1)
	private int correctOpt = 0;

	public String getKeyAsString() {
		return String.valueOf(this.newsArticleQuizId);
	}
}
