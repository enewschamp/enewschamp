package com.enewschamp.app.scores.dto;

import java.io.Serializable;
import java.util.List;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ScorePageData extends PageData implements Serializable {
	private static final long serialVersionUID = 1L;
	private String yearMonth;
	List<DailyScoreDTO> dailyScores;
	List<MonthlyScoreGenreDTO> monthlyScoresByGenre;

}
