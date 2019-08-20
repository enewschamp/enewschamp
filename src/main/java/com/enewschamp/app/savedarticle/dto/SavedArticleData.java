package com.enewschamp.app.savedarticle.dto;

import java.time.LocalDate;
import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.page.dto.ListOfValuesItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SavedArticleData extends PageData{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long newsArticleId;
	private LocalDate publicationDate;
	private String genreId;
	private String headline;
	private List<ListOfValuesItem> genreLOV;
	private List<ListOfValuesItem> monthsLOV;
	private String opinionText;
	
}
