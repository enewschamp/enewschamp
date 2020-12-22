package com.enewschamp.app.article.page.dto;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ArticlePageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long newsArticleId;
	private String likeFlag;
	private String opinionText;
	private String saveFlag;
	private String genre;
	private String headline;
	private String month;

}
