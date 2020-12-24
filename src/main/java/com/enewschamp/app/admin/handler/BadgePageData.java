package com.enewschamp.app.admin.handler;

import java.time.LocalDateTime;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BadgePageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long badgeId = 0L;

	@NotNull
	private String nameId;
    @NotNull
	private String genreId;
	private Long monthlyPointsToScore;
	private String editionId;
	@NotNull
	private int readingLevel;
	private String imageName;
	@Transient
	private String base64Image;
	@JsonInclude
	private String recordInUse;
	@JsonInclude
	private String operator;
	@JsonInclude
	private LocalDateTime lastUpdate;
}
