package com.enewschamp.app.trends.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TrendsYearlyPageData extends PageData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String yearMonth;
	private List<MonthlyArticleDTO> monthlyNewsArticles;
	private List<MonthlyQuizScoresDTO> monthlyQuizScores;
	private List<YearlyArticlesGenreDTO> yearlyArticlesByGenre;
	private List<YearlyQuizGenreDTO> yearlyQuizByGenre ; 
	
}
