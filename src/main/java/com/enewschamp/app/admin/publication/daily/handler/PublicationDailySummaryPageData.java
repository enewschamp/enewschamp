package com.enewschamp.app.admin.publication.daily.handler;

import java.time.LocalDate;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationDailySummaryPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private LocalDate publicationDate;
	private Integer newsArticleCount;
	private Integer quizCount;
	private int month;
	private int year;

}
