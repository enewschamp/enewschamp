package com.enewschamp.publication.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.page.dto.ListOfValuesItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReassignPublisherPageData extends PageData {

	private static final long serialVersionUID = 8751809310808873225L;

	private NewsArticleGroupDTO newsArticleGroup;

	private List<ListOfValuesItem> publisherLOV;
}
