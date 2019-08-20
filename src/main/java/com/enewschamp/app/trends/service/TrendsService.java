package com.enewschamp.app.trends.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.trends.dto.DailyArticleDTO;
import com.enewschamp.app.trends.dto.DailyQuizScoresDTO;
import com.enewschamp.app.trends.dto.MonthlyArticleDTO;
import com.enewschamp.app.trends.dto.MonthlyArticlesGenreDTO;
import com.enewschamp.app.trends.dto.MonthlyQuizGenreDTO;
import com.enewschamp.app.trends.dto.MonthlyQuizScoresDTO;
import com.enewschamp.app.trends.dto.TrendsSearchData;
import com.enewschamp.app.trends.dto.YearlyArticlesGenreDTO;
import com.enewschamp.app.trends.dto.YearlyQuizGenreDTO;
import com.enewschamp.app.trends.repository.TrendsCustomRepositoryImpl;

@Service
public class TrendsService {

	@Autowired
	TrendsCustomRepositoryImpl trendsCustomRepositoryImpl;
	
	public List<DailyArticleDTO> findDailyNewsArticlesTrend(TrendsSearchData searchRequest)
	{
		return trendsCustomRepositoryImpl.findDailyNewsArticlesTrend(searchRequest);
		
	}
	public List<DailyQuizScoresDTO> findDailyQuizScoreTrend(TrendsSearchData searchRequest) 
	{
		return trendsCustomRepositoryImpl.findDailyQuizScoreTrend(searchRequest);
		
	}
	public List<MonthlyArticlesGenreDTO> findMonthlyArticlesByGenreTrend(TrendsSearchData searchRequest)
	{
		return trendsCustomRepositoryImpl.findMonthlyArticlesByGenreTrend(searchRequest);
		
	}
	public List<MonthlyQuizGenreDTO> findMonthlyQuizScoreByGenre(TrendsSearchData searchRequest){
		
		return trendsCustomRepositoryImpl.findMonthlyQuizScoreByGenre(searchRequest);
		
	}
	
	public List<MonthlyArticleDTO> findMonthlyNewsArticlesTrend(TrendsSearchData searchRequest){
		
		return trendsCustomRepositoryImpl.findMonthlyNewsArticlesTrend(searchRequest);
	}
	public List<MonthlyQuizScoresDTO> findMonthlyQuizScoreTrend(TrendsSearchData searchRequest) {
		return trendsCustomRepositoryImpl.findMonthlyQuizScoreTrend(searchRequest);

	}
	public List<YearlyArticlesGenreDTO> findYearlyArticlesByGenreTrend(TrendsSearchData searchRequest){
		return trendsCustomRepositoryImpl.findYearlyArticlesByGenreTrend(searchRequest);

	}
	public List<YearlyQuizGenreDTO> findYearlyQuizScoreByGenre(TrendsSearchData searchRequest){
		
		return trendsCustomRepositoryImpl.findYearlyQuizScoreByGenre(searchRequest);

	}
}
