package com.enewschamp.article.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.page.dto.ListOfValuesItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NewsArticleGroupPageData extends PageData {

	private static final long serialVersionUID = 4569801004564082142L;

	private NewsArticleGroupDTO newsArticleGroup;
	private List<ListOfValuesItem> editionsLOV;
	private List<ListOfValuesItem> daysLOV;
	private List<ListOfValuesItem> monthsLOV;
	private List<ListOfValuesItem> authorLOV;
	private List<ListOfValuesItem> editorLOV;
	private List<ListOfValuesItem> publisherLOV;
	private List<ListOfValuesItem> genreLOV;
	private List<ListOfValuesItem> articleTypeLOV;
	private List<ListOfValuesItem> cityLOV;
	private List<ListOfValuesItem> isLinked;
}
