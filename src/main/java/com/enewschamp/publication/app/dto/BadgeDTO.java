package com.enewschamp.publication.app.dto;

import com.enewschamp.app.common.MaintenanceDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BadgeDTO extends MaintenanceDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private Long badgeId;

	private String nameId;

	@JsonInclude
	private String genreId;

	private Long monthlyPointsToScore;

	private String editionId;

	private int readingLevel;

	private String base64Image;

	private String base64SuccessImage;

	private String base64AudioFile;

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
