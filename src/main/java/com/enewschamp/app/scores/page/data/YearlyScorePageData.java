package com.enewschamp.app.scores.page.data;

import java.io.Serializable;
import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.scores.dto.StudentScoresYearlyGenreDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyTotalDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class YearlyScorePageData extends PageData implements Serializable {
	private static final long serialVersionUID = 1L;
	List<StudentScoresMonthlyTotalDTO> scoresMonthly;
	List<StudentScoresYearlyGenreDTO> scoresYearlyGenre;
}