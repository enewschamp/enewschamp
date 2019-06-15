package com.enewschamp.article.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsArticleSearchResultData extends PageData {
	
	private static final long serialVersionUID = 8626900754726173462L;

	private List<NewsArticleDTO> newsArticles;
	
	private List<NewsArticleSummaryDTO> newsArticlesSummary;
	
}
