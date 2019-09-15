package com.enewschamp.publication.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import com.enewschamp.domain.common.TransactionEntity;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PublicationMonthlySummary")
public class PublicationMonthlySummary extends TransactionEntity {

	private static final long serialVersionUID = -7063853831579952336L;
	
	@Id
	@Column(name = "RecordId", updatable = false, nullable = false)
	private String recordId;
	
	@NotNull
	@Column(name = "Year")
	private int year = 0;
	
	@NotNull
	@Column(name = "Month")
	private int month = 0;
	
	@NotNull
	@Column(name = "EditionId", length = ForeignKeyColumnLength.EditionId)
	private String editionId;
	
	@NotNull
	@Column(name = "GenreId", length = ForeignKeyColumnLength.GenreId)
	private String genreId;

	@NotNull
	@Column(name = "ReadingLevel")
	private int readingLevel = 0;

	@NotNull
	@Column(name = "NewsArticleCount")
	private Long newsArticleCount = 0L;
	
	@NotNull
	@Column(name = "QuizCount")
	private Long quizCount = 0L;
	
	@NotNull
	@Column(name = "LastUpdatedDateTime")
	protected LocalDateTime lastUpdatedDateTime;
	
	public String getKeyAsString() {
		return this.recordId;
	}
	
	@PrePersist
	@PreUpdate
	public void prePersist() {
		if(operationDateTime == null) {
			operationDateTime = LocalDateTime.now();
		}
		if(lastUpdatedDateTime == null) {
			lastUpdatedDateTime = LocalDateTime.now();
		}
		if(recordId == null) {
			recordId = calculateRecordId();
		}
	}
	
	public String calculateRecordId() {
		String hashString = year + StringUtils.leftPad(String.valueOf(month), 2, "0") + editionId + genreId + readingLevel;
		return DigestUtils.md5DigestAsHex(hashString.getBytes());
	}
	
}
