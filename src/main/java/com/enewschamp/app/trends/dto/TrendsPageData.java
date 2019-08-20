package com.enewschamp.app.trends.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TrendsPageData extends PageData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String yearMonth;
	private List<DailyArticleDTO> dailyNewsArticles;
	private List<DailyQuizScoresDTO> dailyQuizScores;
	private List<MonthlyArticlesGenreDTO> monthlyArticlesByGenre;
	private List<MonthlyQuizGenreDTO> monthlyQuizByGenre ; 
	
}
