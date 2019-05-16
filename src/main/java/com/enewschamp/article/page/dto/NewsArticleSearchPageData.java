package com.enewschamp.article.page.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.page.dto.ListOfValuesItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsArticleSearchPageData extends PageData {

	private static final long serialVersionUID = 7566861143124839543L;
	
	private List<ListOfValuesItem> editionsLOV;
	private List<ListOfValuesItem> daysLOV;
	private List<ListOfValuesItem> monthsLOV;
	private List<ListOfValuesItem> authorLOV;
	private List<ListOfValuesItem> editorLOV;
	private List<ListOfValuesItem> publisherLOV;
	private List<ListOfValuesItem> genreLOV;

}
