package com.enewschamp.app.scores.page.data;

import java.io.Serializable;
import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.student.scores.dto.StudentScoresDailyDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyGenreDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ScorePageData extends PageData implements Serializable {
	private static final long serialVersionUID = 1L;
	private String yearMonth;
	List<StudentScoresDailyDTO> scoresDaily;
	List<StudentScoresMonthlyGenreDTO> scoresMonthlyGenre;

}
