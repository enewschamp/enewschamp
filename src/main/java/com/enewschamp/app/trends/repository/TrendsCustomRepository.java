package com.enewschamp.app.trends.repository;

import java.util.List;

import com.enewschamp.app.trends.dto.DailyArticleDTO;
import com.enewschamp.app.trends.dto.DailyQuizScoresDTO;
import com.enewschamp.app.trends.dto.MonthlyArticleDTO;
import com.enewschamp.app.trends.dto.MonthlyArticlesGenreDTO;
import com.enewschamp.app.trends.dto.MonthlyQuizGenreDTO;
import com.enewschamp.app.trends.dto.MonthlyQuizScoresDTO;
import com.enewschamp.app.trends.dto.TrendsSearchData;
import com.enewschamp.app.trends.dto.YearlyArticlesGenreDTO;
import com.enewschamp.app.trends.dto.YearlyQuizGenreDTO;

public interface TrendsCustomRepository {

	public List<DailyArticleDTO> findDailyNewsArticlesTrend(TrendsSearchData searchRequest);
	public List<DailyQuizScoresDTO> findDailyQuizScoreTrend(TrendsSearchData searchRequest) ;
	public List<MonthlyArticlesGenreDTO> findMonthlyArticlesByGenreTrend(TrendsSearchData searchRequest);
	public List<MonthlyQuizGenreDTO> findMonthlyQuizScoreByGenre(TrendsSearchData searchRequest);
	
	public List<MonthlyArticleDTO> findMonthlyNewsArticlesTrend(TrendsSearchData searchRequest);
	public List<MonthlyQuizScoresDTO> findMonthlyQuizScoreTrend(TrendsSearchData searchRequest) ;
	public List<YearlyArticlesGenreDTO> findYearlyArticlesByGenreTrend(TrendsSearchData searchRequest);
	public List<YearlyQuizGenreDTO> findYearlyQuizScoreByGenre(TrendsSearchData searchRequest);

}
