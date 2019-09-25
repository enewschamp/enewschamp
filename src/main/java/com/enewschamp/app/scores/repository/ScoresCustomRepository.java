package com.enewschamp.app.scores.repository;

import java.util.List;

import com.enewschamp.app.scores.dto.DailyScoreDTO;
import com.enewschamp.app.scores.dto.MonthlyScoreGenreDTO;
import com.enewschamp.app.scores.dto.MonthlyScoresDTO;
import com.enewschamp.app.scores.dto.YearlyScoresGenreDTO;
import com.enewschamp.app.scores.page.data.ScoresSearchData;

public interface ScoresCustomRepository {
	public List<DailyScoreDTO> findDailyScores(ScoresSearchData searchRequest);
	public List<MonthlyScoreGenreDTO> findMonthlyScoresByGenre(ScoresSearchData searchRequest);
	
	public List<YearlyScoresGenreDTO> findYearlyScoresByGenre(ScoresSearchData searchRequest);
	public List<MonthlyScoresDTO> findYMonthlyScores(ScoresSearchData searchRequest);
}
