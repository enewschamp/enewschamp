package com.enewschamp.app.scores.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.scores.dto.StudentScoresYearlyGenreDTO;
import com.enewschamp.app.scores.page.data.ScoresSearchData;
import com.enewschamp.app.scores.repository.ScoresCustomRepositoryImpl;
import com.enewschamp.app.student.scores.dto.StudentScoresDailyDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyGenreDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyTotalDTO;

@Service
public class ScoreService {

	@Autowired
	ScoresCustomRepositoryImpl scoresCustomRepositoryImpl;

	public List<StudentScoresDailyDTO> findScoresDaily(ScoresSearchData searchRequest) {
		return scoresCustomRepositoryImpl.findScoresDaily(searchRequest);
	}

	public List<StudentScoresMonthlyGenreDTO> findScoresMonthlyGenre(ScoresSearchData searchRequest) {
		return scoresCustomRepositoryImpl.findScoresMonthlyGenre(searchRequest);
	}

	public List<StudentScoresMonthlyTotalDTO> findYScoresMonthly(ScoresSearchData searchRequest) {
		return scoresCustomRepositoryImpl.findYScoresMonthly(searchRequest);
	}

	public List<StudentScoresYearlyGenreDTO> findScoresYearlyGenre(ScoresSearchData searchRequest) {
		return scoresCustomRepositoryImpl.findScoresYearlyGenre(searchRequest);
	}

	public List<StudentScoresMonthlyTotalDTO> findMonthWiseTotalScores(ScoresSearchData searchRequest) {
		return scoresCustomRepositoryImpl.findMonthWiseTotalScores(searchRequest);
	}

	public List<StudentScoresMonthlyGenreDTO> findMonthWiseGenreScores(ScoresSearchData searchRequest) {
		return scoresCustomRepositoryImpl.findMonthWiseGenreScores(searchRequest);
	}
}
