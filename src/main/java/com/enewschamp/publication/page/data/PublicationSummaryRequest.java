package com.enewschamp.publication.page.data;

import java.time.LocalDate;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationSummaryRequest extends PageData {

	private static final long serialVersionUID = 2029361603680208757L;
	private LocalDate date;
	private Integer month;
	private Integer year;
	
	private String editionId;
	private String genreId;
	private Integer readingLevel;

	private int pageNumber;
	private int pageSize;

}
