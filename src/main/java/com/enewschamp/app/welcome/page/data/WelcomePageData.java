package com.enewschamp.app.welcome.page.data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.enewschamp.app.common.PageData;

import lombok.Data;

@Data
public class WelcomePageData extends PageData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String editionName;
	private String studentName;
	
	private LocalDateTime lastLoginDatetime;
	private String badgeImage;
	private LocalDate badgeDate;
	private String nextBadgeMessage;
	private String userImage;
	
}
