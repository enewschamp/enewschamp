package com.enewschamp.app.admin.genre.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GenrePageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private Long genreId;
	@NotNull(message = MessageConstants.GENRE_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.GENRE_NAME_NOT_EMPTY)
	private String nameId;
	@JsonInclude
	private String imageName;
	private String base64Image;
	private String imageTypeExt;
}
