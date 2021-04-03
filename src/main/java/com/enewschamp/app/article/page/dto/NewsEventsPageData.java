package com.enewschamp.app.article.page.dto;

import java.util.List;

import com.enewschamp.app.common.CommonFilterData;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PaginationData;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NewsEventsPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private List<ListOfValuesItem> genreLOV;
	@JsonInclude
	private List<ListOfValuesItem> cityLOV;
	@JsonInclude
	private CommonFilterData filter;
	@JsonInclude
	private List<NewsEventsPublicationData> newsArticles;
	@JsonInclude
	private PaginationData pagination;

}
