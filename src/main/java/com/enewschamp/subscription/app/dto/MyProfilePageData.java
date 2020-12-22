package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MyProfilePageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyPicturePageData myPicture;
	private StudentDetailsPageData studentDetails;
	private StudentSchoolPageData schoolDetails;
	private StudentSubscriptionPageData subscription;
	private StudentShareAchievementsPageData shareAchievements;
	private StudentPreferencesPageData preferences;

}
