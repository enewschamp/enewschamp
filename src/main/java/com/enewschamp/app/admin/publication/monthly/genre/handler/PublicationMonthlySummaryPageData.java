package com.enewschamp.app.admin.publication.monthly.genre.handler;

import java.time.LocalDate;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "operatorId", "lastUpdate", "recordInUse"})
public class PublicationMonthlySummaryPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Integer quizPublished;
	private Integer articlesPublished;
	private LocalDate publicationDate;
	private String readingLevel;
	private String yearMonth;
	private String genreId;
}

