package com.enewschamp.app.admin.student.preference.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentPreferencesPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long studentId;
	
	@NotNull(message = MessageConstants.READING_LEVEL_NOT_NULL)
	@NotEmpty(message = MessageConstants.READING_LEVEL_NOT_EMPTY)
	private String readingLevel;
	
	private String dailyPublication;
	private String scoresProgressReports;
	private String alertsNotifications;
	private String commsEmailId;

}
