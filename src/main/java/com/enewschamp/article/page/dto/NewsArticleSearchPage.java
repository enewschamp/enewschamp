package com.enewschamp.article.page.dto;

import com.enewschamp.app.common.Page;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsArticleSearchPage extends Page {

	private static final long serialVersionUID = -7757402617610969769L;	

	private NewsArticleSearchPageData data;

}
