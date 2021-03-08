package com.enewschamp.app.welcome.page.data;

import java.time.LocalDateTime;
import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.recognition.page.data.RecognitionData;
import com.enewschamp.app.scores.dto.StudentScoresMonthlyDTO;
import com.enewschamp.subscription.app.dto.AppearancePageData;
import com.enewschamp.subscription.app.dto.MyPicturePageData;
import com.enewschamp.subscription.app.dto.StudentDetailsPageData;
import com.enewschamp.subscription.app.dto.StudentPreferencesPageData;
import com.enewschamp.subscription.app.dto.StudentSchoolPageData;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WelcomePageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String editionName;

	@JsonInclude
	private Long studentId;

	@JsonInclude
	private String testUser;

	@JsonInclude
	private LocalDateTime lastActivityDatetime;

	@JsonInclude
	private LocalDateTime creationDatetime;

	@JsonInclude
	private AppearancePageData appearance;

	@JsonInclude
	private MyPicturePageData myPicture;

	@JsonInclude
	private StudentDetailsPageData studentDetails;

	@JsonInclude
	private StudentSchoolPageData schoolDetails;

	@JsonInclude
	private StudentSubscriptionPageData subscription;

	@JsonInclude
	private StudentPreferencesPageData preferences;

	@JsonInclude
	private List<RecognitionData> badgeDetails;

	@JsonInclude
	private List<StudentScoresMonthlyDTO> scoresMonthly;
}
