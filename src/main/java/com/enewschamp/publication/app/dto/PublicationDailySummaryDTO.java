package com.enewschamp.publication.app.dto;

import java.time.LocalDate;

import com.enewschamp.app.common.AbstractDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class PublicationDailySummaryDTO extends AbstractDTO {


	private static final long serialVersionUID = 9135020752738860024L;

	private LocalDate publicationDate;

	private Integer newsArticleCount = 0;
	
	private Integer quizCount = 0;
	
	public String getKeyAsString() {
		return String.valueOf(this.publicationDate);
	}
	
}
