package com.enewschamp.app.admin.publication.monthly.total.handler;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "operatorId", "lastUpdate", "recordInUse"})
public class PublicationMonthlyTotalPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Integer quizPublished;
	private Integer articlesPublished;
	private String readingLevel;
	private String yearMonth;
}

