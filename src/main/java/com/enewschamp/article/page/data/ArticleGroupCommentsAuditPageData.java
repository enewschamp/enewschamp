package com.enewschamp.article.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.page.dto.ListOfValuesItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleGroupCommentsAuditPageData extends PageData {

	private static final long serialVersionUID = 397699898871724397L;

	private NewsArticleGroupDTO newsArticleGroup;
	
	private List<ListOfValuesItem> editorLOV;
}
