package com.enewschamp.app.article.page.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PaginationData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationPageData extends PageData {

	private static final long serialVersionUID = 1L;

	@JsonInclude
	private List<PublicationData> newsArticles;

	@JsonInclude
	private Long notificationsCount;

	@JsonInclude
	private PaginationData pagination;
}
