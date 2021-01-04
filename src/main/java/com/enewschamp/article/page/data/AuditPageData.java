package com.enewschamp.article.page.data;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuditPageData extends PageData {

	private static final long serialVersionUID = 2560121552740385594L;

	private Long newsArticleGroupId;
	private Long newsArticleId;
	private Long publicationId;
	private Long publicationGroupId;

	@JsonInclude
	private JsonNode audit;
}
