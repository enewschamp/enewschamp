package com.enewschamp.app.admin.handler;

import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BadgePageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long badgeId = 0L;

	@NotNull(message = MessageConstants.BADGE_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.BADGE_NAME_NOT_EMPTY)
	private String nameId;
	@NotNull(message = MessageConstants.GENRE_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.GENRE_ID_NOT_EMPTY)
	private String genreId;
	private Long monthlyPointsToScore;
	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EDITION_ID_NOT_EMPTY)
	private String editionId;
	@NotNull(message = MessageConstants.READING_LEVEL_NOT_NULL)
	@NotEmpty(message = MessageConstants.READING_LEVEL_NOT_EMPTY)
	private int readingLevel;
	private String imageName;
	@Transient
	private String base64Image;
}
