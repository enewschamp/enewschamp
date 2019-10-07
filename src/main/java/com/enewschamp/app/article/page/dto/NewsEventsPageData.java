package com.enewschamp.app.article.page.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.savedarticle.dto.MonthsLovData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsEventsPageData extends PageData{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<MonthsLovData> monthsLov;
	private NewsEventsFilterData filter;
	private List<NewsEventsPublicationData> newsArticles;

}
