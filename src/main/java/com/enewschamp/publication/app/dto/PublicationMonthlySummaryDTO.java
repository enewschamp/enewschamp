package com.enewschamp.publication.app.dto;

import java.time.LocalDateTime;

import com.enewschamp.app.common.TransactionDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class PublicationMonthlySummaryDTO extends TransactionDTO {

	private static final long serialVersionUID = 4193168914061747727L;

	private String recordId;

	private int year = 0;

	private int month = 0;

	private String editionId;

	private String genreId;

	private int readingLevel = 0;

	private Long newsArticleCount = 0L;

	private Long quizCount = 0L;

	protected LocalDateTime lastUpdatedDateTime;

}
