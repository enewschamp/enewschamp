package com.enewschamp.publication.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.TransactionEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PublicationDailySummary")
public class PublicationDailySummary extends TransactionEntity {


	private static final long serialVersionUID = 9135020752738860024L;

	@Id
	@Column(name = "PublicationDate")
	private LocalDate publicationDate;

	@NotNull
	@Column(name = "NewsArticleCount")
	private Integer newsArticleCount = 0;
	
	@NotNull
	@Column(name = "QuizCount")
	private Integer quizCount = 0;
	
	@NotNull
	@Column(name = "Month")
	private int month = 0;
	
	@NotNull
	@Column(name = "Year")
	private int year = 0;
	
	
	public String getKeyAsString() {
		return String.valueOf(this.publicationDate);
	}

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if(operationDateTime == null) {
			operationDateTime = LocalDateTime.now();
		}
		month = publicationDate.getMonthValue();
		year = publicationDate.getYear();
	}
}
