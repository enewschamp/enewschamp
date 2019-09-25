package com.enewschamp.app.scores.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.scores.dto.DailyScoreDTO;
import com.enewschamp.app.scores.dto.MonthlyScoreGenreDTO;
import com.enewschamp.app.scores.dto.MonthlyScoresDTO;
import com.enewschamp.app.scores.dto.YearlyScoresGenreDTO;
import com.enewschamp.app.scores.page.data.ScoresSearchData;
import com.enewschamp.app.scores.repository.ScoresCustomRepositoryImpl;
import com.enewschamp.app.trends.dto.TrendsSearchData;

@Service
public class ScoreTrendService {

	@Autowired
	ScoresCustomRepositoryImpl scoresCustomRepositoryImpl;
	
	public List<DailyScoreDTO> findDailyScores(ScoresSearchData searchRequest){
		return scoresCustomRepositoryImpl.findDailyScores(searchRequest);
	}
	public List<MonthlyScoreGenreDTO> findMonthlyScoresByGenre(ScoresSearchData searchRequest) {
		return scoresCustomRepositoryImpl.findMonthlyScoresByGenre(searchRequest);
	}

	
	public List<YearlyScoresGenreDTO> findYearlyScoresByGenre(ScoresSearchData searchRequest) {
		return scoresCustomRepositoryImpl.findYearlyScoresByGenre(searchRequest);
	}

	
	public List<MonthlyScoresDTO> findYMonthlyScores(ScoresSearchData searchRequest) {
		return scoresCustomRepositoryImpl.findYMonthlyScores(searchRequest);
	}
}
