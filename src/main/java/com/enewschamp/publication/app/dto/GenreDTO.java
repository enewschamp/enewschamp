package com.enewschamp.publication.app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GenreDTO extends AbstractDTO {

	private static final long serialVersionUID = -3428291258057090659L;

	@NotNull
	@Size(min=1, max=12)
	private String genreId;
	
	@NotNull
	private Long nameId;
	
	@NotNull
	@Size(min=1, max=200)
	private String imagePath;
	
}
