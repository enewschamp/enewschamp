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

	private Long badgeId;

	private String nameId;

	private String genreId;

	private Long monthlyPointsToScore;

	private String editionId;

	private int readingLevel;

	private String base64Image;

	@JsonInclude
	private String imageName;
}
