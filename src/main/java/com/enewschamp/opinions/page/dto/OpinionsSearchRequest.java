package com.enewschamp.opinions.page.dto;

import java.time.LocalDate;
import java.util.List;

import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OpinionsSearchRequest {

	private String editionId;
	private String genre;
	private String headline;
	private Long studentId;
	private LocalDate publicationDate;
	private LocalDate publicationDateFrom;
	private LocalDate publicationDateTo;
	private List<NewsArticleSummaryDTO> opinionsArticles;
}
