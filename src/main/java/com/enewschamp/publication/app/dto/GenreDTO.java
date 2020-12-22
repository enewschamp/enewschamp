package com.enewschamp.publication.app.dto;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GenreDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -3428291258057090659L;

	private Long genreId;

	@NotNull
	private String nameId;

	private String base64Image;

	private String genreName;

	@JsonInclude
	private String imageName;

}
