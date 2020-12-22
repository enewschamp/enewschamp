package com.enewschamp.app.welcome.page.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import com.enewschamp.app.common.PageData;
import com.enewschamp.subscription.app.dto.MyPicturePageData;
import com.enewschamp.subscription.app.dto.StudentDetailsPageData;
import com.enewschamp.subscription.app.dto.StudentPreferencesPageData;
import com.enewschamp.subscription.app.dto.StudentSchoolPageData;
import com.enewschamp.subscription.app.dto.StudentShareAchievementsPageData;
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
	private LocalDateTime lastActivityDatetime;

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
	private List<BadgeDetailsDTO> badgeDetails;
}
