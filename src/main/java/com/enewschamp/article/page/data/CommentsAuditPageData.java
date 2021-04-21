package com.enewschamp.article.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommentsAuditPageData extends PageData {

	private static final long serialVersionUID = 8861564911059174596L;

	private Long newsArticleGroupId;

	private Long newsArticleId;

	private Long publicationId;

	private List<PropertyAuditData> previousComments;

}
