package com.enewschamp.app.savedarticle.dto;

import java.time.LocalDate;
import java.util.List;

import com.enewschamp.domain.common.MonthType;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=false)
public class SavedNewsArticleSearchRequest {

	
	private String editionId;
	private String genreId;
	private String headline;
	private Long studentId;
	
	private LocalDate publicationDate;
	private MonthType intendedPubMonth;
	
	private List<SavedNewsArticleSummaryDTO> savedArtivles;
}
