package com.enewschamp.app.article.page.dto;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsEventsSearchData extends PageData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String city;
	private String month;
	private String headline;
	
}
