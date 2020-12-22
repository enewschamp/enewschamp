package com.enewschamp.app.scores.dto;

import java.io.Serializable;
import java.util.List;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class YearlyScorePageData extends PageData implements Serializable {
	private static final long serialVersionUID = 1L;
	List<YearlyScoresGenreDTO> yearlyScoresByGenre;
	List<MonthlyScoresDTO> monthlyScores;
}
