package com.enewschamp.article.page.dto;

import com.enewschamp.app.common.PageData;
import com.enewschamp.page.dto.SelectOption;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsArticleSearchPageData extends PageData {

	private static final long serialVersionUID = 7566861143124839543L;
	
	private SelectOption[] editionsLOV;
	private SelectOption[] daysLOV;
	private SelectOption[] monthsLOV;
	private SelectOption[] authorLOV;
	private SelectOption[] editorLOV;
	private SelectOption[] publisherLOV;
	private SelectOption[] genreLOV;

}
