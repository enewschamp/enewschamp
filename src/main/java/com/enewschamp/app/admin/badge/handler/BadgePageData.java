package com.enewschamp.app.admin.badge.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BadgePageData extends PageData {
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private Long badgeId;

	@NotNull(message = MessageConstants.BADGE_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.BADGE_NAME_NOT_EMPTY)
	private String nameId;

	@JsonInclude
	@NotNull(message = MessageConstants.GENRE_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.GENRE_ID_NOT_EMPTY)
	private String genreId;

	private Long monthlyPointsToScore;

	private String editionId;
	@NotNull(message = MessageConstants.READING_LEVEL_NOT_NULL)
	private int readingLevel;

	private String imageBase64;

	private String successImageBase64;

	private String audioFileBase64;

	private String imageTypeExt = "jpg";

	private String successImageTypeExt = "jpg";

	private String audioFileTypeExt = "mp4";

	@JsonInclude
	private String imageName;

	private String imageUpdate;

	@JsonInclude
	private String successImageName;

	private String successImageUpdate;

	@JsonInclude
	private String audioFileName;

	private String audioFileUpdate;
}
