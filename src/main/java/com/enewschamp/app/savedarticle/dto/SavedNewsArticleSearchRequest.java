package com.enewschamp.app.savedarticle.dto;

import java.time.LocalDate;
import java.util.List;

import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.domain.common.MonthType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SavedNewsArticleSearchRequest {

	private String editionId;
	private String genre;
	private String headline;
	private Long studentId;
	private String yearMonth;
	private LocalDate publicationDate;
	private LocalDate publicationDateFrom;
	private LocalDate publicationDateTo;
	private MonthType intendedPubMonth;
	private List<NewsArticleSummaryDTO> savedArticles;
}
