package com.enewschamp.app.admin.publication.monthly.handler;

import java.time.LocalDateTime;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationMonthlySummaryPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private String recordId;
	private int year;
	private int month;
	private String editionId;
	private String genreId;
	private int readingLevel;
	private Long newsArticleCount;
	private Long quizCount;
	protected LocalDateTime lastUpdatedDateTime;

}
