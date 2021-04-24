package com.enewschamp.app.admin.celebration.handler;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CelebrationPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long celebrationId = 0L;

	private String nameId;

	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EDITION_ID_NOT_EMPTY)
	private String editionId;

	@NotNull(message = MessageConstants.DATE_NOT_NULL)
	private LocalDate date;

	@NotNull(message = MessageConstants.OCCASION_NOT_NULL)
	@NotEmpty(message = MessageConstants.OCCASION_NOT_EMPTY)
	private String occasion;

	@NotNull(message = MessageConstants.READING_LEVEL_NOT_NULL)
	private int readingLevel;

	private String imageBase64;
	private String imageTypeExt = "jpg";
	@JsonInclude
	private String imageName;
	private String imageUpdate;

	private String audioFileBase64;
	private String audioFileTypeExt = "mp4";
	@JsonInclude
	private String audioFileName;
	private String audioFileUpdate;
	private String gender;
}
