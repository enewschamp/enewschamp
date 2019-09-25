package com.enewschamp.app.article.page.dto;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ArticlePageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long newsArticleId;
	private String likeFlag;
	private String opinion;
	private String saveFlag;
	private String genreId;
private String headline;
private String headlineKeyWord;
private String publishMonth;

	
}
