package com.enewschamp.app.savedarticle.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
public class SavedNewsArticleSummaryDTO {
	private Long newsArticleId;
	private LocalDate publicationDate;
	private String genreId;
	private String headLine;
	private String opinionText;
	
	
}
