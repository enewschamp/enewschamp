package com.enewschamp.app.scores.repository;

import java.util.List;

import com.enewschamp.app.scores.dto.StudentScoresYearlyGenreDTO;
import com.enewschamp.app.scores.page.data.ScoresSearchData;
import com.enewschamp.app.student.scores.dto.StudentScoresDailyDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyGenreDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyTotalDTO;

public interface ScoresCustomRepository {

	public List<StudentScoresDailyDTO> findScoresDaily(ScoresSearchData searchRequest);

	public List<StudentScoresMonthlyGenreDTO> findScoresMonthlyGenre(ScoresSearchData searchRequest);

	public List<StudentScoresMonthlyTotalDTO> findYScoresMonthly(ScoresSearchData searchRequest);

	public List<StudentScoresYearlyGenreDTO> findScoresYearlyGenre(ScoresSearchData searchRequest);

	public List<StudentScoresMonthlyTotalDTO> findMonthWiseTotalScores(ScoresSearchData searchRequest);

	public List<StudentScoresMonthlyGenreDTO> findMonthWiseGenreScores(ScoresSearchData searchRequest);
}