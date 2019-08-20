package com.enewschamp.app.savedarticle.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.page.dto.ListOfValuesItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SavedArticlePageData extends PageData{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ListOfValuesItem> monthsLOV;
	private List<ListOfValuesItem> genreLOV;
	private List<SavedArticleData> savedNewsArticles;

}
