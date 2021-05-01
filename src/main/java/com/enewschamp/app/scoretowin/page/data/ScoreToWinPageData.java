package com.enewschamp.app.scoretowin.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.recognition.page.data.RecognitionData;
import com.enewschamp.app.scores.dto.StudentScoresMonthlyDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ScoreToWinPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private List<RecognitionData> badgeDetails;

	@JsonInclude
	private List<StudentScoresMonthlyDTO> scoresMonthly;
}
